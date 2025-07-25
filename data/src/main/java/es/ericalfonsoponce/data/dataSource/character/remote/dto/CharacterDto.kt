package es.ericalfonsoponce.data.dataSource.character.remote.dto

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: CharacterOriginDto,
    val location: CharacterLocationDto,
    val image: String
)

