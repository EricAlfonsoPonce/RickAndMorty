package es.ericalfonsoponce.presentation.xml.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterUiState(
    val characters: List<CharacterShow> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingPagination: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharacterUiState())
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()

    private var page: Int = 1
    private var hasNextPage: Boolean = true

    fun getCharacters(isLoadingPagination: Boolean = false) {
        if (isLoadingPagination.not()) setIsLoading(true) else setIsLoadingPagination(true)
        viewModelScope.launch {
            characterUseCase.getCharactersByPage(page = page)
                .onSuccess { pairResult ->
                    hasNextPage = pairResult.first
                    if (pairResult.first) page++
                    _uiState.update { it.copy(characters = it.characters + pairResult.second) }
                }
                .onFailure {

                }
            if (isLoadingPagination.not()) setIsLoading(false) else setIsLoadingPagination(false)
        }
    }

    fun refreshData(){
        page = 1
        _uiState.update { it.copy(characters = emptyList()) }
        getCharacters()
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun setIsLoadingPagination(isLoading: Boolean) {
        _uiState.update { it.copy(isLoadingPagination = isLoading) }
    }
}