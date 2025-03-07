package sp.kx.math

import java.util.Objects

class MutableOffset(
    override var dX: Double,
    override var dY: Double,
    override var dZ: Double,
) : Offset {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Offset -> dX == other.dX && dY == other.dY && dZ == other.dZ
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(dX, dY, dZ)
    }
}
