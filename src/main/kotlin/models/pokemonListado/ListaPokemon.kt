package models.pokemonListado

import kotlinx.serialization.Serializable

@Serializable
data class ListaPokemon(
    val results: List<Result>
)