package services.ktorFit

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import models.Pokemon

interface KtorFitRest {
    @GET("pokemon/{id}") // El id puede ser o bien un id literal o un nombre
    suspend fun getById(@Path("id") id: String): Pokemon
}