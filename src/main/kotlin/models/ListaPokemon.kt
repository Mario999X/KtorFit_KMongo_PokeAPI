package models

data class ListaPokemon(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)