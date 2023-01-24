package repositories

import kotlinx.coroutines.*
import models.pokemonIndividual.Pokemon
import services.cache.PokemonCache

class CacheRepository(
    private val cachePokemon: PokemonCache
) : CrudRepository<Pokemon, String> {

    private var refreshJob: Job? = null // Job para cancelar la ejecución

    private var listaBusquedas =
        mutableListOf<Pokemon>() // Lista donde se almacenaran los datos que se introduciran en cache

    init {
        refreshCache()
    }

    override suspend fun findById(id: String): Pokemon? {
        var pokemon: Pokemon? = null

        cachePokemon.cache.asMap().forEach {
            if (it.key == id || it.value.name == id.trim().lowercase()) {
                pokemon = it.value
            }
        }
        println("\t🔎findById")
        return pokemon
    }

    override fun findAll(): List<Pokemon> {
        println("\t📖findAll")
        return cachePokemon.cache.asMap().values.toList()
    }

    override fun delete(entity: Pokemon): Boolean {
        println("\t👉delete")
        var existe = false
        val pokemon = cachePokemon.cache.asMap()[entity.id]
        if (pokemon != null) {
            // Por como lo tengo programado, tengo que eliminar tambien los datos que coincidan en la lista de busquedas
            listaBusquedas.removeIf { it.id == pokemon.id }
            cachePokemon.cache.invalidate(entity.id)
            existe = true
        }
        return existe
    }

    override fun update(entity: Pokemon): Pokemon {
        TODO("Not yet implemented")
    }

    override fun save(entity: Pokemon): Pokemon {
        println("\t✍Saving $entity")
        listaBusquedas.add(entity)
        return entity
    }

    private fun refreshCache() {
        if (refreshJob != null) refreshJob?.cancel()

        refreshJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                println("\tComprobando cache...")
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