package es.ericalfonsoponce.presentation.compose.screens.home

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
    val isRefreshing: Boolean = false,
    val error: AppError? = null
)

sealed class HomeScreenActions {
    class DeleteCharacter(val character: CharacterShow): HomeScreenActions()
    object OnLoadMoreCharacters: HomeScreenActions()
    object OnRefresh: HomeScreenActions()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()
    private var page: Int = 1
    private var hasNextPage: Boolean = true

    init {
        loadCharacters()
    }

    fun loadCharacters(isRefreshing: Boolean = false, isLoadingPagination: Boolean = false) {
        when {
            isRefreshing -> setIsRefreshing(true)
            isLoadingPagination -> setIsLoadingPagination(true)
            else -> setIsLoading(true)
        }
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
            when {
                isRefreshing -> setIsRefreshing(false)
                isLoadingPagination -> setIsLoadingPagination(false)
                else -> setIsLoading(false)
            }
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
        if (hasNextPage) loadCharacters(isLoadingPagination = true)
    }

    fun refreshData() {
        page = 1
        hasNextPage = true
        _uiState.update {
            it.copy(
                isRefreshing = true,
                characters = emptyList()
            )
        }
        loadCharacters(isRefreshing = true)
    }

    fun onCharacterUpdated(character: CharacterShow){
        _uiState.update {
            it.copy(
                characters = it.characters.toMutableList().map { characterShow ->
                    if(characterShow.id == character.id) {
                        character
                    } else {
                        characterShow
                    }
                }
            )
        }
    }

    fun resetError(){
        _uiState.update {
            it.copy(
                error = null
            )
        }
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading,
                isRefreshing = false,
                isLoadingPagination = false
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

    private fun setIsRefreshing(isRefreshing: Boolean) {
        _uiState.update {
            it.copy(
                isRefreshing = isRefreshing,
                isLoading = isRefreshing
            )
        }
    }
}