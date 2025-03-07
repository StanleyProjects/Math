package sp.kx.math

import java.util.Objects

@Suppress("ktlint:standard:parameter-list-wrapping")
class MutableMatrix(
    override var m00: Double, override var m10: Double, override var m20: Double, override var m30: Double,
    override var m01: Double, override var m11: Double, override var m21: Double, override var m31: Double,
    override var m02: Double, override var m12: Double, override var m22: Double, override var m32: Double,
    override var m03: Double, override var m13: Double, override var m23: Double, override var m33: Double,
) : Matrix {
    constructor() : this(
        m00 = 0.0, m01 = 0.0, m02 = 0.0, m03 = 0.0,
        m10 = 0.0, m11 = 0.0, m12 = 0.0, m13 = 0.0,
        m20 = 0.0, m21 = 0.0, m22 = 0.0, m23 = 0.0,
        m30 = 0.0, m31 = 0.0, m32 = 0.0, m33 = 0.0,
    )

    @Suppress("ktlint:standard:indent")
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

fun Matrix.mut(): MutableMatrix {
    return MutableMatrix(
        m00 = m00, m01 = m01, m02 = m02, m03 = m03,
        m10 = m10, m11 = m11, m12 = m12, m13 = m13,
        m20 = m20, m21 = m21, m22 = m22, m23 = m23,
        m30 = m30, m31 = m31, m32 = m32, m33 = m33,
    )
}

@Suppress("ktlint:standard:wrapping")
fun MutableMatrix.mul(other: Matrix) {
    val m00 = this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20 + this.m03 * other.m30
    val m01 = this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21 + this.m03 * other.m31
    val m02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22 + this.m03 * other.m32
    val m03 = this.m00 * other.m03 + this.m01 * other.m13 + this.m02 * other.m23 + this.m03 * other.m33
    //
    val m10 = this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20 + this.m13 * other.m30
    val m11 = this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21 + this.m13 * other.m31
    val m12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22 + this.m13 * other.m32
    val m13 = this.m10 * other.m03 + this.m11 * other.m13 + this.m12 * other.m23 + this.m13 * other.m33
    //
    val m20 = this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20 + this.m23 * other.m30
    val m21 = this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21 + this.m23 * other.m31
    val m22 = this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22 + this.m23 * other.m32
    val m23 = this.m20 * other.m03 + this.m21 * other.m13 + this.m22 * other.m23 + this.m23 * other.m33
    //
    val m30 = this.m30 * other.m00 + this.m31 * other.m10 + this.m32 * other.m20 + this.m33 * other.m30
    val m31 = this.m30 * other.m01 + this.m31 * other.m11 + this.m32 * other.m21 + this.m33 * other.m31
    val m32 = this.m30 * other.m02 + this.m31 * other.m12 + this.m32 * other.m22 + this.m33 * other.m32
    val m33 = this.m30 * other.m03 + this.m31 * other.m13 + this.m32 * other.m23 + this.m33 * other.m33
    //
    this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03
    this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13
    this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23
    this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33
}
