package es.ericalfonsoponce.presentation.xml.character

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.presentation.xml.R
import es.ericalfonsoponce.presentation.xml.databinding.ActivityCharacterDetailBinding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailActivity : AppCompatActivity() {

    private var binding: ActivityCharacterDetailBinding? = null
    private val viewModel: CharacterDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        binding?.activity = this
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        setContentView(binding?.root)

        initSpinners()
        initObservers()
        viewModel.checkIntent(intent.extras)
    }

    private fun initSpinners(){
        binding?.let { binding ->
            val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGender.adapter = genderAdapter

            val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerStatus.adapter = statusAdapter
        }
    }

    private fun initObservers(){
        viewModel.character.observe(this) { character ->
            binding?.let { binding ->
                Glide.with(this)
                    .load(character.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageCharacter)

                binding.spinnerGender.setSelection(genders.indexOf(character.gender))
                binding.spinnerStatus.setSelection(statuses.indexOf(character.status))
            }
        }
    }

    fun closeActivity(){
        setResult(RESULT_CANCELED)
        finish()
    }

    companion object{
        val genders = listOf(CharacterGender.FEMALE, CharacterGender.MALE, CharacterGender.GENDERLESS, CharacterGender.UNKNOWN)
        val statuses = listOf(CharacterStatus.ALIVE, CharacterStatus.DEAD, CharacterStatus.UNKNOWN)
    }
}