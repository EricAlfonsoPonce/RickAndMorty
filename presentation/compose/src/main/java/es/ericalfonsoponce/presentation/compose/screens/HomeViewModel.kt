package es.ericalfonsoponce.presentation.compose.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUiState(
    val characters: List<CharacterShow> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingPagination: Boolean = false,
    val error: AppError? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()
    private var page: Int = 1
    private var hasNextPage: Boolean = true

    fun getCharacters(isLoadingPagination: Boolean = false) {
        if (isLoadingPagination.not()) setIsLoading(true) else setIsLoadingPagination(true)
        viewModelScope.launch {
            characterUseCase.getCharactersByPage(page = page)
                .onSuccess { pairResult ->
                    hasNextPage = pairResult.first
                    if (pairResult.first) page++
                    _uiState.update {
                        it.copy(
                            characters = it.characters.toMutableList().plus(pairResult.second)
                        )
                    }
                }
                .onFailure { throwable ->

                    _uiState.update { it.copy(error = throwable as AppError) }
                }
            if (isLoadingPagination.not()) setIsLoading(false) else setIsLoadingPagination(false)
        }
    }

    fun deleteCharacter(character: CharacterShow) {
        viewModelScope.launch {
            characterUseCase.removeCharacter(character)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            characters = it.characters.toMutableList().apply { remove(character) })
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(error = throwable as AppError) }
                }
        }
    }

    fun loadNextPage() {
        if (hasNextPage) getCharacters(true)
    }

    fun refreshData() {
        page = 1
        _uiState.update {
            it.copy(
                characters = emptyList()
            )
        }
        getCharacters()
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private fun setIsLoadingPagination(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoadingPagination = isLoading
            )
        }
    }
}