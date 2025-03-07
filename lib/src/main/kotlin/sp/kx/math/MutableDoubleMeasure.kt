package sp.kx.math

import java.util.Objects

class MutableDoubleMeasure(
    var magnitude: Double,
) : Measure<Double, Double> {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is MutableDoubleMeasure -> magnitude == other.magnitude
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(magnitude)
    }

    override fun transform(units: Double): Double {
        return units * magnitude
    }

    override fun units(value: Double): Double {
        return value / magnitude
    }
}
