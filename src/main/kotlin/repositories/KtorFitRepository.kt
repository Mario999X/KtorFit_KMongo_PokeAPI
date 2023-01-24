package repositories

import models.pokemonIndividual.Pokemon
import models.pokemonListado.Result
import services.ktorFit.KtorFitClient

class KtorFitRepository {

    // Inyectar dependencia
    private val client by lazy { KtorFitClient.instance }

    suspend fun findAll(): List<Result>? {
        return try {
            val listado = client.getAll()
            println("\t📖📖📖findAll")
            listado.results
        } catch (e: Exception) {
            System.err.println("Excepcion: " + e.message)
            null
        }
    }

    suspend fun findById(id: String): Pokemon? {
        return try {
            val pokemonInApi = client.getById(id)
            println("\t🔎🔎🔎findById")
            pokemonInApi
        } catch (e: Exception) {
            System.err.println("Excepcion: " + e.message)
            null
        }
    }

}