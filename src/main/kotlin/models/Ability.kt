package models

import kotlinx.serialization.Serializable

@Serializable
data class Ability(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)