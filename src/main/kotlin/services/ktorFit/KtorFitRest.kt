package services.ktorFit

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import models.pokemonIndividual.Pokemon
import models.pokemonListado.ListaPokemon

interface KtorFitRest {
    @GET("pokemon/{id}") // El id puede ser o bien un id literal o un nombre
    suspend fun getById(@Path("id") id: String): Pokemon

    @GET("pokemon?limit=100000&offset=0")
    suspend fun getAll(): ListaPokemon
}