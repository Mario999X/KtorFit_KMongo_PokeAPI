package repositories

import kotlinx.coroutines.*
import models.Pokemon
import services.cache.PokemonCache
import services.ktorFit.KtorFitClient

class RepositoryPokemon(
    private val cachePokemon: PokemonCache
) {
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
        return pokemon
    }

    suspend fun findById(id: String): Pokemon? {
        return findByIdInCache(id)
            ?: try { // if (callCache == null)
                val call = client.getById(id)
                listaBusquedas.add(call)
                call
            } catch (e: Exception) {
                System.err.println("Excepcion: " + e.message)
                null
            }
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

                    println("\tCache actualizada: ${cachePokemon.cache.asMap().size}")
                    delay(cachePokemon.refreshTime.toLong())
                } else {
                    println("\tCache actual: ${cachePokemon.cache.asMap().size}")
                    delay(cachePokemon.refreshTime.toLong())
                }
            }
        }
    }
}