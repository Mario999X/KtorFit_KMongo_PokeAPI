import db.MongoDbManager
import kotlinx.coroutines.runBlocking
import models.Pokemon
import org.litote.kmongo.getCollection
import repositories.RepositoryPokemon
import services.cache.PokemonCache
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val repository = RepositoryPokemon(PokemonCache())

    limpiarDatos()

    do {
        println("Introduzca un ID/Nombre a buscar: ")
        val data = readln()

        measureTimeMillis {
            val pokemonData = repository.findById(data.trim().lowercase())

            if (pokemonData == null) {
                System.err.println("ID/NOMBRE NO EXISTE: $data")
            } else println(pokemonData)

        }.also {
            println("Tiempo de ejecucion: $it ms")
        }

    } while (true)
}

private fun limpiarDatos() {
    if (MongoDbManager.database.getCollection<Pokemon>().countDocuments() > 0) {
        MongoDbManager.database.getCollection<Pokemon>().drop()
    }
}