package es.ericalfonsoponce.presentation.xml.character

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
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

    fun checkIntent(bundle: Bundle?) {
        bundle?.let { bundle ->
            val extra =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    bundle.getSerializable("Character", CharacterShow::class.java)
                else bundle.getSerializable("Character") as? CharacterShow
            extra?.let {
                _character.value = it
            } ?: run {_error.value = AppError.Unknown }
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
}