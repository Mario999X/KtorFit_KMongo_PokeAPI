package controllers

import kotlinx.coroutines.*
import models.pokemonIndividual.Pokemon
import models.pokemonListado.Result
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
        return@withContext pokemonSearch
    }

    fun getAllPokemonCache(): List<Pokemon> {
        return cacheRepository.findAll()
    }

    fun getAllPokemonMongo(): List<Pokemon> {
        return mongoRepository.findAll()
    }

    suspend fun getAllPokemonAPI(): List<Result>? {
        return ktorFitRepository.findAll()
    }

    suspend fun savePokemon(entity: Pokemon) = withContext(Dispatchers.IO) {
        // Lo almacenamos tanto en cache como en Mongo
        launch {
            cacheRepository.save(entity)
        }

        launch {
            mongoRepository.save(entity)
        }

        joinAll()
    }

    suspend fun deletePokemonById(entity: Pokemon) = withContext(Dispatchers.IO) {
        // Lo eliminamos tanto en cache como en Mongo
        launch {
            cacheRepository.delete(entity)
        }
        launch {
            mongoRepository.delete(entity)
        }

        joinAll()
    }
}