package es.ericalfonsoponce.presentation.xml.character

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import javax.inject.Inject


@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {

    private val _character = MutableLiveData<CharacterShow>()
    val character: LiveData<CharacterShow> = _character

    fun checkIntent(bundle: Bundle?) {
        bundle?.let { bundle ->
            val extra =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    bundle.getSerializable("Character", CharacterShow::class.java)
                else bundle.getSerializable("Character") as? CharacterShow
            extra?.let {
                _character.value = it
            } ?: "" // ERROR
        } ?: run {
            // ERROR
        }
    }

    fun saveChanges() {

    }
}