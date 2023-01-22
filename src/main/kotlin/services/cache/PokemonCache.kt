package services.cache

import io.github.reactivecircus.cache4k.Cache
import models.Pokemon
import kotlin.time.Duration.Companion.minutes

class PokemonCache {
    val refreshTime = 30000 // 30 segundos

    val cache = Cache.Builder()
        .expireAfterAccess(1.minutes)
        .maximumCacheSize(10)
        .build<String, Pokemon>()
}