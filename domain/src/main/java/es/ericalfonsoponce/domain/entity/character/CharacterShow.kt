package es.ericalfonsoponce.domain.entity.character

import java.io.Serializable
import kotlinx.serialization.Serializable as KotlinSerializable

@KotlinSerializable
data class CharacterShow(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: CharacterGender,
    val origin: String,
    val location: String,
    val image: String
): Serializable

@KotlinSerializable
enum class CharacterStatus (val value: String): Serializable{
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("Unknown")
}

@KotlinSerializable
enum class CharacterGender (val value: String): Serializable {
    FEMALE("Female"),
    MALE("Male"),
    GENDERLESS("Genderless"),
    UNKNOWN("Unknown")
}