package repositories

import models.Pokemon
import services.ktorFit.KtorFitClient

class KtorFitRepository : CrudRepository<Pokemon, String> {

    // Inyectar dependencia
    private val client by lazy { KtorFitClient.instance }

    override fun findAll(): List<Pokemon> {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Pokemon): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(entity: Pokemon): Pokemon {
        TODO("Not yet implemented")
    }

    override fun save(entity: Pokemon): Pokemon {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): Pokemon? {
        return try {
            val pokemonInApi = client.getById(id)
            println("\t🔎🔎🔎findByIdAPI")
            pokemonInApi
        } catch (e: Exception) {
            System.err.println("Excepcion: " + e.message)
            null
        }
    }

}