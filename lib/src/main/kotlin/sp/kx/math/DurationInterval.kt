package sp.kx.math

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun Interval<Duration>.diff(): Duration {
    return b - a
}

fun Interval<Duration>.frequency(value: Duration = 1.seconds): Double {
    return value / (b - a)
}
