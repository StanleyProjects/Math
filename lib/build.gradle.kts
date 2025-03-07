import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sp.gx.core.Badge
import sp.gx.core.GitHub
import sp.gx.core.Markdown
import sp.gx.core.Maven
import sp.gx.core.asFile
import sp.gx.core.assemble
import sp.gx.core.buildDir
import sp.gx.core.check
import sp.gx.core.create
import sp.gx.core.existing
import sp.gx.core.file
import sp.gx.core.filled
import sp.gx.core.task
import kotlin.time.Duration.Companion.seconds

version = "0.1.0"

val maven = Maven.Artifact(
    group = "com.github.kepocnhh",
    id = rootProject.name,
)

val gh = GitHub.Repository(
    owner = "StanleyProjects",
    name = rootProject.name,
)

repositories.mavenCentral()

plugins {
    id("org.gradle.jacoco")
    id("org.jetbrains.kotlin.jvm")
}

val compileKotlinTask = tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions {
        jvmTarget = Version.jvmTarget
        freeCompilerArgs = freeCompilerArgs + setOf("-module-name", maven.moduleName())
    }
}

tasks.getByName<JavaCompile>("compileTestJava") {
    targetCompatibility = Version.jvmTarget
}

tasks.getByName<KotlinCompile>("compileTestKotlin") {
    kotlinOptions.jvmTarget = Version.jvmTarget
}

sourceSets.create("jmh") {
    project.kotlin.target.compilations.also {
        it[name].associateWith(it["main"])
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Version.jupiter}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Version.jupiter}")
    "jmhImplementation"("org.openjdk.jmh:jmh-core:${Version.jmh}")
    "jmhImplementation"("org.openjdk.jmh:jmh-generator-bytecode:${Version.jmh}")
}

fun Test.getExecutionData(): File {
    return buildDir()
        .dir("jacoco")
        .asFile("$name.exec")
}

val taskUnitTest = task<Test>("checkUnitTest") {
    useJUnitPlatform()
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED") // https://github.com/gradle/gradle/issues/18647
    doLast {
        getExecutionData().existing().file().filled()
    }
}

jacoco.toolVersion = Version.jacoco

val taskCoverageReport = task<JacocoReport>("assembleCoverageReport") {
    dependsOn(taskUnitTest)
    reports {
        csv.required = false
        html.required = true
        xml.required = false
    }
    sourceDirectories.setFrom(file("src/main/kotlin"))
    classDirectories.setFrom(sourceSets.main.get().output.classesDirs)
    executionData(taskUnitTest.getExecutionData())
    doLast {
        val report = buildDir()
            .dir("reports/jacoco/$name/html")
            .file("index.html")
            .existing()
            .file()
            .filled()
        println("Coverage report: ${report.absolutePath}")
    }
}

task<JacocoCoverageVerification>("checkCoverage") {
    dependsOn(taskCoverageReport)
    violationRules {
        rule {
            limit {
                minimum = BigDecimal(0.96)
            }
        }
    }
    classDirectories.setFrom(taskCoverageReport.classDirectories)
    executionData(taskCoverageReport.executionData)
}

project.kotlin.target.compilations.getByName("jmh") {
    val issuer = name
    val dir = buildDir().dir("${issuer}Generated")
    val outputSourceDir = dir.asFile("sources")
    val outputResourceDir = dir.asFile("resources")
    val outputClassesDir = dir.dir("classes")
    val generatorType = "default"
    val generators = output.classesDirs.map {
        val compiledBytecodePath = it.absolutePath
        // Usage: generator <compiled-bytecode-dir> <output-source-dir> <output-resource-dir> [generator-type]
        task<JavaExec>("${issuer}RunBytecodeGenerator${compiledBytecodePath.hashCode()}") {
            dependsOn("classes")
            mainClass.set("org.openjdk.jmh.generators.bytecode.JmhBytecodeGenerator")
            classpath = sourceSets[issuer].runtimeClasspath
            args(
                compiledBytecodePath,
                outputSourceDir.absolutePath,
                outputResourceDir.absolutePath,
                generatorType,
            )
        }
    }
    val compileGeneratedTask = task<JavaCompile>("${issuer}CompileGenerated") {
        dependsOn(generators)
        classpath = sourceSets[issuer].runtimeClasspath
        source(outputSourceDir)
        destinationDirectory.set(outputClassesDir)
    }
    task<JavaExec>("runBenchmark") {
        val benchmarks: String? by project
        dependsOn(compileGeneratedTask)
        val reports = buildDir().asFile("reports/jmh")
        doFirst {
            reports.mkdirs()
        }
        mainClass.set("org.openjdk.jmh.Main")
        classpath(
            sourceSets[issuer].runtimeClasspath,
            outputResourceDir,
            outputClassesDir,
        )
        val timeout = 10.seconds
        val iterations = 1
        val time = 1.seconds
        val forks = 1
        val wf = 1
        val wi = 1
        val wt = 1.seconds
        val mode = "AverageTime"
        val format = "text"
        val output = reports.resolve("result.txt")
        args(
            benchmarks.orEmpty(),
            "-to=${timeout.inWholeMilliseconds}ms",
            "-f=$forks",
            "-i=$iterations",
            "-r=${time.inWholeMilliseconds}ms",
            "-wf=$wf",
            "-wi=$wi",
            "-w=${wt.inWholeMilliseconds}ms",
            "-bm=$mode",
//            "-prof=cl",
//            "-prof=comp",
            "-rf=$format",
            "-rff=${output.absolutePath}",
            "-t=max",
        )
    }
}

"unstable".also { variant ->
    val version = "${version}u-SNAPSHOT"
    tasks.create("check", variant, "Readme") {
        doLast {
            val badge = Markdown.image(
                text = "version",
                url = Badge.url(
                    label = "version",
                    message = version,
                    color = "2962ff",
                ),
            )
            val expected = setOf(
                badge,
                Markdown.link("Maven", Maven.Snapshot.url(maven, version)),
                "implementation(\"${maven.moduleName(version)}\")",
            )
            rootDir.resolve("README.md").check(
                expected = expected,
                report = buildDir()
                    .dir("reports/analysis/readme")
                    .asFile("index.html"),
            )
        }
    }
    tasks.create("assemble", variant, "MavenMetadata") {
        doLast {
            val file = buildDir()
                .dir("yml")
                .file("maven-metadata.yml")
                .assemble(
                    """
                        repository:
                         groupId: '${maven.group}'
                         artifactId: '${maven.id}'
                        version: '$version'
                    """.trimIndent(),
                )
            println("Metadata: ${file.absolutePath}")
        }
    }
    task<Jar>("assemble", variant, "Jar") {
        dependsOn(compileKotlinTask)
        archiveBaseName = maven.id
        archiveVersion = version
        from(compileKotlinTask.destinationDirectory.asFileTree)
    }
    task<Jar>("assemble", variant, "Source") {
        archiveBaseName = maven.id
        archiveVersion = version
        archiveClassifier = "sources"
        from(sourceSets.main.get().allSource)
    }
    tasks.create("assemble", variant, "Pom") {
        doLast {
            val file = buildDir()
                .dir("libs")
                .file("${maven.name(version)}.pom")
                .assemble(
                    maven.pom(
                        version = version,
                        packaging = "jar",
                    ),
                )
            println("POM: ${file.absolutePath}")
        }
    }
}
