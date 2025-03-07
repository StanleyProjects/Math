package sp.kx.math

interface Measure<T : Any, U : Any> {
    fun transform(units: T): U
    fun units(value: U): T
}
