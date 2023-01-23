package controllers

import kotlinx.coroutines.*
import models.Pokemon
import repositories.CacheRepository
import repositories.KtorFitRepository
import repositories.MongoRepository

class PokemonController(
    private val cacheRepository: CacheRepository,
    private val mongoRepository: MongoRepository,
    private val ktorFitRepository: KtorFitRepository
) {
    // POKEMONES
    suspend fun getPokemonById(id: String): Pokemon? = withContext(Dispatchers.IO) {
        var pokemonSearch = cacheRepository.findById(id)
        if (pokemonSearch == null) {
            pokemonSearch = mongoRepository.findById(id)
            if (pokemonSearch == null) {
                pokemonSearch = ktorFitRepository.findById(id)
            }
        }
        if (pokemonSearch != null) {
            // Al obtenerlo, lo almacenamos tanto en cache como en Mongo
            launch {
                cacheRepository.save(pokemonSearch)
            }

            launch {
                mongoRepository.save(pokemonSearch)
            }

            joinAll()
        }
        return@withContext pokemonSearch
    }

    suspend fun deletePokemonById(entity: Pokemon) = withContext(Dispatchers.IO) {
        launch {
            deletePokemonByIdCache(entity)
        }
        launch {
            deletePokemonByIdMongo(entity)
        }

        joinAll()
    }

    private fun deletePokemonByIdCache(entity: Pokemon): Boolean {
        return cacheRepository.delete(entity)
    }

    private fun deletePokemonByIdMongo(entity: Pokemon): Boolean {
        return mongoRepository.delete(entity)
    }

}