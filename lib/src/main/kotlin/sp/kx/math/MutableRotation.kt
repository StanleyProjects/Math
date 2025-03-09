package sp.kx.math

class MutableRotation(
    override var aX: Double,
    override var aY: Double,
    override var aZ: Double,
) : Rotation

fun Rotation.mut(): MutableRotation {
    return MutableRotation(
        aX = aX,
        aY = aY,
        aZ = aZ,
    )
}
