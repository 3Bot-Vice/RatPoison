package rat.poison.game

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import rat.poison.game.entity.EntityType
import rat.poison.game.entity.Player
import rat.poison.settings.MAX_ENTITIES
import rat.poison.utils.collections.CacheableList
import rat.poison.utils.collections.ListContainer
import java.util.*

@Volatile
var me: Player = 0
@Volatile
var clientState: ClientState = 0

typealias EntityContainer = ListContainer<EntityContext>
typealias EntityList = Object2ObjectArrayMap<EntityType, CacheableList<EntityContext>>

private val cachedResults = Int2ObjectArrayMap<EntityContainer>(EntityType.size)

val entitiesValues = arrayOfNulls<CacheableList<EntityContext>>(MAX_ENTITIES)
var entitiesValuesCounter = 0

val entities: Object2ObjectMap<EntityType, CacheableList<EntityContext>>
		= EntityList(EntityType.size).apply {
	for (type in EntityType.cachedValues) {
		val cacheableList = CacheableList<EntityContext>(MAX_ENTITIES)
		put(type, cacheableList)
		entitiesValues[entitiesValuesCounter++] = cacheableList
	}
}

fun entityByType(type: EntityType): EntityContext? = entities[type]?.firstOrNull()

internal inline fun forEntities(types: Array<EntityType> = EntityType.cachedValues,
                                crossinline body: (EntityContext) -> Boolean): Boolean {
	val hash = types.contentHashCode()
	val container = cachedResults.get(hash) ?: EntityContainer(EntityType.size)

	if (container.empty()) {
		for (type in types) if (type != EntityType.NULL) {
			val cacheableList = entities[type]!!
			container.addList(cacheableList)
			cachedResults[hash] = container
		}
	}

	return container.forEach(body)
}