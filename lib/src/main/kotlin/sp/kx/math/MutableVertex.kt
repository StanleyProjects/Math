package sp.kx.math

import java.util.Objects

class MutableVertex(
    override var x: Double,
    override var y: Double,
    override var z: Double,
) : Vertex {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Vertex -> x == other.x && y == other.y && z == other.z
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y, z)
    }
}
