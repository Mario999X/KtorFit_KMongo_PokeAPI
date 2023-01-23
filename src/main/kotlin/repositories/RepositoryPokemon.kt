package repositories

import db.MongoDbManager
import kotlinx.coroutines.*
import models.Pokemon
import org.litote.kmongo.*
import services.cache.PokemonCache
import services.ktorFit.KtorFitClient

class RepositoryPokemon(
    private val cachePokemon: PokemonCache
) : CrudRepository<Pokemon, String> {
    // Inyectar dependencia
    private val client by lazy { KtorFitClient.instance }

    private var refreshJob: Job? = null // Job para cancelar la ejecución

    private var listaBusquedas = mutableListOf<Pokemon>()

    init {
        refreshCache()
    }

    private fun findByIdInCache(id: String): Pokemon? {
        var pokemon: Pokemon? = null

        cachePokemon.cache.asMap().forEach {
            if (it.key == id || it.value.name == id.trim().lowercase()) {
                pokemon = it.value
            }
        }
        println("\t✔findByIdInCache")
        return pokemon
    }

    private fun findByIdMongo(id: String): Pokemon? {
        println("\t✔✔findByIdMongo")
        return MongoDbManager.database.getCollection<Pokemon>().findOneById(id)
    }

    override suspend fun findById(id: String): Pokemon? {
        val pokemonInCache = findByIdInCache(id)

        if (pokemonInCache == null) {
            val pokemonInMongo = findByIdMongo(id)
            //println("\t\t\t" + pokemonInMongo) // Tuve que indicar con @BsonId en el modelo para evitar problemas.
            if (pokemonInMongo == null) {
                try {
                    val pokemonInApi = client.getById(id)
                    // Agregamos en la lista para el cache
                    listaBusquedas.add(pokemonInApi)
                    // Agregamos en MongoDB
                    save(pokemonInApi)

                    println("\t✔✔✔findByIdAPI")
                    return pokemonInApi
                } catch (e: Exception) {
                    System.err.println("Excepcion: " + e.message)
                    return null
                }
            } else return pokemonInMongo
        } else {
            return pokemonInCache
        }

    }

    override fun findAll(): List<Pokemon> {
        println("\tfindAll")
        return MongoDbManager.database.getCollection<Pokemon>().find().toList()
    }

    override fun delete(entity: Pokemon): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(entity: Pokemon): Pokemon {
        TODO("Not yet implemented")
    }

    override fun save(entity: Pokemon): Pokemon {
        println("\tSaving $entity")
        MongoDbManager.database.getCollection<Pokemon>().save(entity)
        return entity
    }

    private fun refreshCache() {
        if (refreshJob != null) refreshJob?.cancel()

        refreshJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                println("\tActualizando cache...")
                if (listaBusquedas.isNotEmpty()) {
                    listaBusquedas.forEach {
                        cachePokemon.cache.put(it.id, it)
                    }

                    listaBusquedas.clear()

                    println("\tCache actualizada: ${cachePokemon.cache.asMap().size}\n")
                    delay(cachePokemon.refreshTime.toLong())
                } else {
                    println("\tCache actual: ${cachePokemon.cache.asMap().size}\n")
                    delay(cachePokemon.refreshTime.toLong())
                }
            }
        }
    }
}