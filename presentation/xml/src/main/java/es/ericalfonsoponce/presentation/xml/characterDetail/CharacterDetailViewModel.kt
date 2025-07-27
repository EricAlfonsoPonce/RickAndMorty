package es.ericalfonsoponce.presentation.xml.characterDetail

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import es.ericalfonsoponce.presentation.xml.characterDetail.CharacterDetailActivity.Companion.INTENT_CHARACTER
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {

    private val _character = MutableLiveData<CharacterShow>()
    val character: LiveData<CharacterShow> = _character

    private val _characterUpdateSuccessful = MutableLiveData<Boolean>()
    val characterUpdateSuccessful: LiveData<Boolean> = _characterUpdateSuccessful

    private val _error = MutableLiveData<AppError>()
    val error: LiveData<AppError> = _error

    val genders = listOf(
        CharacterGender.FEMALE,
        CharacterGender.MALE,
        CharacterGender.GENDERLESS,
        CharacterGender.UNKNOWN
    )
    val statuses = listOf(CharacterStatus.ALIVE, CharacterStatus.DEAD, CharacterStatus.UNKNOWN)

    fun checkIntent(bundle: Bundle?) {
        bundle?.let { bundle ->
            val extra =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    bundle.getSerializable(INTENT_CHARACTER, CharacterShow::class.java)
                else bundle.getSerializable(INTENT_CHARACTER) as? CharacterShow
            extra?.let {
                _character.value = it
            } ?: run { _error.value = AppError.Unknown }
        } ?: run {
            _error.value = AppError.Unknown
        }
    }

    fun saveChanges() {
        _character.value?.let { character ->
            viewModelScope.launch {
                characterUseCase.updateCharacter(character)
                    .onSuccess {
                        _characterUpdateSuccessful.value = true
                    }
                    .onFailure { throwable ->
                        _error.value = throwable as AppError
                    }
            }
        }
    }

    fun updateCharacterName(name: String) {
        _character.value = character.value?.copy(name = name)
    }

    fun updateCharacterSpecie(specie: String) {
        _character.value = character.value?.copy(species = specie)
    }

    fun updateCharacterStatus(position: Int) {
        _character.value = character.value?.copy(status = statuses[position])
    }

    fun updateCharacterGender(position: Int) {
        _character.value = character.value?.copy(gender = genders[position])
    }

    fun getCharacterStatusIndex(status: CharacterStatus): Int {
        return statuses.indexOf(status)
    }

    fun getCharacterGenderIndex(gender: CharacterGender): Int {
        return genders.indexOf(gender)
    }
}