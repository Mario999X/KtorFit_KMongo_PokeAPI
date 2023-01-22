import kotlinx.coroutines.runBlocking
import repositories.RepositoryPokemon
import services.cache.PokemonCache
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val repository = RepositoryPokemon(PokemonCache())

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