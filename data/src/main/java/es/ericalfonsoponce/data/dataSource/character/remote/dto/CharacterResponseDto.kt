package es.ericalfonsoponce.data.dataSource.character.remote.dto

data class CharacterResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)
