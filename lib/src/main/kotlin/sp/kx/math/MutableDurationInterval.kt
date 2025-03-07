package sp.kx.math

import java.util.Objects
import kotlin.time.Duration

class MutableDurationInterval(
    override var a: Duration,
    override var b: Duration,
) : Interval<Duration> {
    override fun equals(other: Any?): Boolean {
        if (other !is Interval<*>) return false
        if (other.a !is Duration) return false
        if (other.b !is Duration) return false
        return a == other.a && b == other.b
    }

    override fun hashCode(): Int {
        return Objects.hash(a, b)
    }
}
