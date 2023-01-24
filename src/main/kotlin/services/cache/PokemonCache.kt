package services.cache

import io.github.reactivecircus.cache4k.Cache
import models.pokemonIndividual.Pokemon
import kotlin.time.Duration.Companion.seconds

class PokemonCache {
    val refreshTime = 20000 // 20 segundos

    val cache = Cache.Builder()
        .expireAfterAccess(30.seconds)
        .maximumCacheSize(10)
        .build<String, Pokemon>()
}