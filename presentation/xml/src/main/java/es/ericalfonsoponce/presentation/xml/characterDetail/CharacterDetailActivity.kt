package es.ericalfonsoponce.presentation.xml.characterDetail

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.presentation.xml.databinding.ActivityCharacterDetailBinding

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
        initListeners()
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

    private fun initListeners(){
        binding?.spinnerGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                viewModel.character.value?.gender = genders[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding?.spinnerStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                viewModel.character.value?.status = statuses[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
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

        viewModel.characterUpdateSuccessful.observe(this){ isSuccess ->
            if(isSuccess){
                setResult(
                    RESULT_OK,
                    Intent().putExtra("Character", viewModel.character.value)
                )
                finish()
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