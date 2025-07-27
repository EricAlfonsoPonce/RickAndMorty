package es.ericalfonsoponce.presentation.compose.screens.characterDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import es.ericalfonsoponce.presentation.compose.navigation.Screens
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterDetailScreenUiState(
    val character: CharacterShow? = null,
    val error: AppError? = null
)

sealed class NavEvent {
    class NavigateToHomeScreen(val character: CharacterShow) : NavEvent()
}

sealed class CharacterDetailScreenActions {
    object OnSetCharacterStatus : CharacterDetailScreenActions()
    object OnSetCharacterGender : CharacterDetailScreenActions()
    object OnSetCharacterName : CharacterDetailScreenActions()
    object OnSetCharacterSpecie : CharacterDetailScreenActions()
    object SaveChanges : CharacterDetailScreenActions()
}

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharacterDetailScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _navEventChannel = Channel<NavEvent>()
    val navEventsFlow = _navEventChannel.receiveAsFlow()

    init {
        _uiState.update {
            it.copy(
                character = Screens.CharacterDetailScreen.from(savedStateHandle).character
            )
        }
    }

    fun setCharacterStatus(status: CharacterStatus) {
        _uiState.update { it.copy(character = it.character?.copy(status = status)) }
    }

    fun setCharacterGender(gender: CharacterGender) {
        _uiState.update { it.copy(character = it.character?.copy(gender = gender)) }
    }

    fun setCharacterName(name: String) {
        _uiState.update { it.copy(character = it.character?.copy(name = name)) }
    }

    fun setCharacterSpecie(specie: String) {
        _uiState.update { it.copy(character = it.character?.copy(species = specie)) }
    }

    fun resetError() {
        _uiState.update {
            it.copy(
                error = null
            )
        }
    }

    fun saveChanges() {
        _uiState.value.character?.let { character ->
            viewModelScope.launch {
                characterUseCase.updateCharacter(character)
                    .onSuccess {
                        _navEventChannel.send(NavEvent.NavigateToHomeScreen(character))
                    }
                    .onFailure { throwable ->
                        _uiState.update {
                            it.copy(
                                error = throwable as AppError
                            )
                        }
                    }
            }
        }
    }
}