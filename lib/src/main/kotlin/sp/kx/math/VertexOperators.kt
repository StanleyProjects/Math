package sp.kx.math

operator fun Vertex.times(measure: Measure<Double, Double>): Vertex {
    return MutableVertex(
        x = measure.transform(x),
        y = measure.transform(y),
        z = measure.transform(z),
    )
}

operator fun Vertex.div(measure: Measure<Double, Double>): Vertex {
    return MutableVertex(
        x = measure.units(x),
        y = measure.units(y),
        z = measure.units(z),
    )
}

operator fun Vertex.plus(offset: Offset): Vertex {
    return MutableVertex(
        x = x + offset.dX,
        y = y + offset.dY,
        z = z + offset.dZ,
    )
}

operator fun Vertex.times(matrix: Matrix): Vertex {
    return MutableVertex(
        x = matrix.m00 * x + matrix.m01 * y + matrix.m02 * z + matrix.m03,
        y = matrix.m10 * x + matrix.m11 * y + matrix.m12 * z + matrix.m13,
        z = matrix.m20 * x + matrix.m21 * y + matrix.m22 * z + matrix.m23,
    )
}
