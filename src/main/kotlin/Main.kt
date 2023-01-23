import controllers.PokemonController
import db.MongoDbManager
import kotlinx.coroutines.runBlocking
import models.Pokemon
import org.litote.kmongo.getCollection
import repositories.CacheRepository
import repositories.KtorFitRepository
import repositories.MongoRepository
import services.cache.PokemonCache
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

private val cache = PokemonCache()
private val controller = PokemonController(CacheRepository(cache), MongoRepository(), KtorFitRepository())

fun main() = runBlocking {

    limpiarDatos()

    do {
        println(
            """
            
            Introduzca la opcion:
            1. Buscar Pokemon
            2. Eliminar Pokemon
        """.trimIndent()
        )
        val data = readln()

        println("Introduzca un ID/Nombre: ")
        val data2 = readln()

        measureTimeMillis {
            if (data == "1") {
                searchPokemon(data2)
            } else if (data == "2") {
                val pokemon = searchPokemon(data2)
                if (pokemon != null) {
                    controller.deletePokemonById(pokemon)
                }
            } else exitProcess(1)

        }.also {
            println("Tiempo de ejecucion: $it ms")
        }

    } while (true)
}

private suspend fun searchPokemon(id: String): Pokemon? {
    val pokemonData = controller.getPokemonById(id)

    if (pokemonData == null) {
        System.err.println("ID/NOMBRE NO EXISTE: $id")
    } else println(pokemonData)

    return pokemonData
}

private fun limpiarDatos() {
    if (MongoDbManager.database.getCollection<Pokemon>().countDocuments() > 0) {
        MongoDbManager.database.getCollection<Pokemon>().drop()
    }
}