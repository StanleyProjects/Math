package sp.kx.math

operator fun Size.times(measure: Measure<Double, Double>): Size {
    return MutableSize(
        width = measure.transform(width),
        height = measure.transform(height),
    )
}

operator fun Size.div(measure: Measure<Double, Double>): Size {
    return MutableSize(
        width = measure.units(width),
        height = measure.units(height),
    )
}

fun Size.center(dZ: Double): Offset {
    return MutableOffset(
        dX = width / 2,
        dY = height / 2,
        dZ = dZ,
    )
}
