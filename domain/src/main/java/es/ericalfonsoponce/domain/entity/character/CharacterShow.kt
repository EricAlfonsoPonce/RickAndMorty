package es.ericalfonsoponce.domain.entity.character

import java.io.Serializable

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

enum class CharacterStatus (val value: String){
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("Unknown")
}

enum class CharacterGender (val value: String){
    FEMALE("Female"),
    MALE("Male"),
    GENDERLESS("Genderless"),
    UNKNOWN("Unknown")
}