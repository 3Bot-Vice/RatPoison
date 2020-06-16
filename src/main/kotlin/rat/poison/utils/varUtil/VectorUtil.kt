package rat.poison.utils.varUtil

import com.badlogic.gdx.math.Vector3
import rat.poison.utils.Angle
import rat.poison.utils.Vector

operator fun Vector3.minus(v: Vector3): Vector3 {
    x - v.x
    y - v.y
    z - v.z
    return this
}

operator fun Vector3.plus(v: Vector3): Vector3 {
    x + v.x
    y + v.y
    z + v.z
    return this
}

operator fun Vector3.times(f: Float): Vector3 {
    x * f
    y * f
    z * f
    return this
}

operator fun Vector3.divAssign(f: Float) {
    x /= f
    y /= f
    z /= f
}

fun Angle.toVector3(): Vector3 {
    val vec = Vector3()
    vec.x = x.toFloat(); vec.y = y.toFloat(); vec.z = z.toFloat()
    return vec
}

fun Vector3.toVector(): Vector {
    return Vector(x.toDouble(), y.toDouble(), z.toDouble())
}