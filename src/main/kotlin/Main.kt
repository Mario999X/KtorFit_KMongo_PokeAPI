import controllers.PokemonController
import db.MongoDbManager
import kotlinx.coroutines.runBlocking
import models.pokemonIndividual.Pokemon
import org.litote.kmongo.getCollection
import repositories.CacheRepository
import repositories.KtorFitRepository
import repositories.MongoRepository
import services.cache.PokemonCache
import kotlin.system.measureTimeMillis

private val cache = PokemonCache()
private val controller = PokemonController(CacheRepository(cache), MongoRepository(), KtorFitRepository())

private lateinit var data2: String

fun main() = runBlocking {

    limpiarDatos()

    do {
        println(
            """
            
            Introduzca la opcion:
            1. Buscar Pokemon
            2. Eliminar Pokemon
            3. Obtener todos los Pokemon
        """.trimIndent()
        )
        val data = readln()

        if (data < "3") {
            println("Introduzca un ID/Nombre: ")
            data2 = readln()

        } else {
            measureTimeMillis {

                if (data == "1") {
                    searchPokemon(data2)

                } else if (data == "2") {
                    val pokemon = searchPokemon(data2)

                    if (pokemon != null) {
                        deletePokemon(pokemon)
                    }
                } else if (data == "3") {
                    obtainAllPokemon()
                } else System.err.println("Fallo en la lectura de teclado...")

            }.also {
                println("Tiempo de ejecucion: $it ms")
            }
        }


    } while (true)
}

private suspend fun obtainAllPokemon() {
    val listado = controller.getAllPokemon()
    println(listado)
}

private suspend fun deletePokemon(entity: Pokemon) {
    controller.deletePokemonById(entity)
}

suspend fun searchPokemon(id: String): Pokemon? {
    val pokemonData = controller.getPokemonById(id)

    if (pokemonData == null) {
        System.err.println("ID/NOMBRE NO EXISTE: $id")
    } else {
        controller.savePokemon(pokemonData)
        println(
            """
            -----------
            $pokemonData
            -----------
        """.trimIndent()
        )
    }

    return pokemonData
}

private fun limpiarDatos() {
    if (MongoDbManager.database.getCollection<Pokemon>().countDocuments() > 0) {
        MongoDbManager.database.getCollection<Pokemon>().drop()
    }
}