package sp.kx.math

import java.util.Objects

class MutableSize(
    override var width: Double,
    override var height: Double,
) : Size {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Size -> width == other.width && height == other.height
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(width, height)
    }
}
