package rat.poison.game

import com.badlogic.gdx.math.MathUtils.clamp
import rat.poison.curSettings
import rat.poison.game.CSGO.csgoEXE
import rat.poison.game.entity.Player
import rat.poison.game.entity.position
import rat.poison.game.entity.punch
import rat.poison.game.netvars.NetVarOffsets.vecViewOffset
import rat.poison.utils.*
import rat.poison.utils.varUtil.strToBool
import rat.poison.utils.varUtil.toInt
import java.lang.Math.toDegrees
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.sqrt

fun getCalculatedAngle(player: Player, dst: Vector): Angle {
	val ang = Angle()

	val myPunch = player.punch()
	val myPosition = player.position()

	val dX = myPosition.x - dst.x
	val dY = myPosition.y - dst.y
	val dZ = myPosition.z + csgoEXE.float(player + vecViewOffset) - dst.z

	val hyp = sqrt((dX * dX) + (dY * dY))

	val rcsXVariation = curSettings["AIM_RCS_VARIATION"].toDouble()
	val rcsYVariation = curSettings["AIM_RCS_VARIATION"].toDouble()

	if (curSettings["FACTOR_RECOIL"].strToBool()) {
		if (curSettings["AIM_ADVANCED"].strToBool()) {
			val randX = if (rcsXVariation > 0.0) randDouble(0.0, rcsXVariation) * randBoolean().toInt() else 0.0
			val randY = if (rcsYVariation > 0.0) randDouble(0.0, rcsYVariation) * randBoolean().toInt() else 0.0
			val calcX = toDegrees(atan(dZ / hyp)) - myPunch.x * clamp(1.0 + curSettings["AIM_RCS_Y"].toDouble() + randX, 1.0, 2.0)
			val calcY = toDegrees(atan(dY / dX)) - myPunch.y * clamp(1.0 + curSettings["AIM_RCS_X"].toDouble() + randY, 1.0, 2.0)
			ang.x = calcX
			ang.y = calcY

		} else {
			ang.x = toDegrees(atan(dZ / hyp)) - myPunch.x * 2.0 //Move these above IFs
			ang.y = toDegrees(atan(dY / dX)) - myPunch.y * 2.0
		}
	} else {
		ang.x = toDegrees(atan(dZ / hyp))//Move these above IFs
		ang.y = toDegrees(atan(dY / dX))
	}

	ang.z = 0.0
	if (dX >= 0.0) ang.y += 180

	ang.normalize()

	return ang
}

fun realCalcAngle(player: Player, dst: Vector): Angle {
	val playerPos = player.position()
	val delta = Vector(dst.x - playerPos.x, dst.y - playerPos.y, dst.z - playerPos.z + csgoEXE.float(player + vecViewOffset))

	val myPunch = me.punch()

	val aX = toDegrees(atan2(-delta.z, sqrt(delta.x*delta.x + delta.y*delta.y))) - myPunch.x * 2.0
	val aY = toDegrees(atan2(delta.y, delta.x)) - myPunch.y * 2.0

	val ang = Angle(aX, aY, 0.0)
	ang.normalize()

	return ang
}