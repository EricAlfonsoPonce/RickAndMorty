package es.ericalfonsoponce.domain.entity.character

import java.io.Serializable

data class CharacterShow(
    val id: Int,
    var name: String,
    var status: CharacterStatus,
    val species: String,
    var gender: CharacterGender,
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