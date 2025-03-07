package sp.kx.math

import java.util.Objects

class MutableMatrix(
    override var m00: Double, override var m10: Double,
    override var m01: Double, override var m11: Double,
    override var m02: Double, override var m12: Double,
    override var m03: Double, override var m13: Double,
    override var m20: Double, override var m30: Double,
    override var m21: Double, override var m31: Double,
    override var m22: Double, override var m32: Double,
    override var m23: Double, override var m33: Double,
) : Matrix {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Matrix -> {
                m00 == other.m00 && m10 == other.m10 && m20 == other.m20 && m30 == other.m30 &&
                m01 == other.m01 && m11 == other.m11 && m21 == other.m21 && m31 == other.m31 &&
                m02 == other.m02 && m12 == other.m12 && m22 == other.m22 && m32 == other.m32 &&
                m03 == other.m03 && m13 == other.m13 && m23 == other.m23 && m33 == other.m33
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(
            m00, m10, m20, m30,
            m01, m11, m21, m31,
            m02, m12, m22, m32,
            m03, m13, m23, m33,
        )
    }
}
