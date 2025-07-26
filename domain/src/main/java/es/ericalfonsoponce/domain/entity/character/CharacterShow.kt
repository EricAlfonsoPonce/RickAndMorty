package es.ericalfonsoponce.domain.entity.character

data class CharacterShow(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val origin: String,
    val location: String,
    val image: String
)

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