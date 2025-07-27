package es.ericalfonsoponce.presentation.xml.home

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
class HomeViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {
    private val _characters = MutableLiveData<List<CharacterShow>>(emptyList())
    val characters: LiveData<List<CharacterShow>> = _characters

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingPagination = MutableLiveData<Boolean>()
    val isLoadingPagination: LiveData<Boolean> = _isLoadingPagination

    private val _error = MutableLiveData<AppError>()
    val error: LiveData<AppError> = _error

    private var page: Int = 1
    private var hasNextPage: Boolean = true

    fun getCharacters(isLoadingPagination: Boolean = false) {
        if (isLoadingPagination.not()) setIsLoading(true) else setIsLoadingPagination(true)
        viewModelScope.launch {
            characterUseCase.getCharactersByPage(page = page)
                .onSuccess { pairResult ->
                    hasNextPage = pairResult.first
                    if (pairResult.first) page++
                    _characters.value = _characters.value?.toMutableList()?.plus(pairResult.second)
                }
                .onFailure { throwable ->
                    _error.value = throwable as AppError
                }
            if (isLoadingPagination.not()) setIsLoading(false) else setIsLoadingPagination(false)
        }
    }

    fun deleteCharacter(character: CharacterShow){
        viewModelScope.launch {
            characterUseCase.removeCharacter(character)
                .onSuccess {
                    _characters.value = _characters.value?.toMutableList()?.apply {
                        remove(character)
                    }
                }
                .onFailure { throwable ->
                    _error.value = throwable as AppError
                }
        }
    }

    fun loadNextPage() {
        if (hasNextPage) getCharacters(true)
    }

    fun refreshData() {
        page = 1
        _characters.value = emptyList()
        getCharacters()
    }

    private fun setIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    private fun setIsLoadingPagination(isLoading: Boolean) {
        _isLoadingPagination.value = isLoading
    }
}