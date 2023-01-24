package models.pokemonIndividual

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Pokemon(
    @BsonId
    val id: String,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val order: Int,
    val weight: Int,
    val abilities: List<Ability>

) {
    override fun toString(): String {
        return "Pokemon(id=$id, name='$name', baseExperience=$base_experience, height=$height, order=$order, weight=$weight, lista habilidades: \n$abilities)"
    }

}
