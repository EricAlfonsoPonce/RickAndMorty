package es.ericalfonsoponce.presentation.xml.characterDetail

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.presentation.xml.R
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.presentation.xml.R
import es.ericalfonsoponce.presentation.xml.components.showSimpleDialog
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

        binding?.let {
            WindowInsetsControllerCompat(window, it.root).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }

        setContentView(binding?.root)

        initSpinners()
        initListeners()
        initObservers()
        viewModel.checkIntent(intent.extras)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initSpinners() {
        binding?.let { binding ->
            val genderAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.genders)
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGender.adapter = genderAdapter

            val statusAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.statuses)
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerStatus.adapter = statusAdapter
        }
    }

    private fun initListeners() {
        binding?.spinnerGender?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.updateCharacterGender(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding?.spinnerStatus?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.updateCharacterStatus(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding?.editNameCharacter?.doOnTextChanged { text, _, _, _ ->
            viewModel.updateCharacterName(text.toString())
        }

        binding?.editSpecieCharacter?.doOnTextChanged { text, _, _, _ ->
            viewModel.updateCharacterSpecie(text.toString())
        }
    }

    private fun initObservers() {
        viewModel.character.observe(this) { character ->
            binding?.let { binding ->
                Glide.with(this)
                    .load(character.image)
                    .placeholder(R.drawable.img_placeholder)
                    .fallback(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageCharacter)

                binding.spinnerGender.setSelection(viewModel.getCharacterGenderIndex(character.gender))
                binding.spinnerStatus.setSelection(viewModel.getCharacterStatusIndex(character.status))
            }
        }

        viewModel.characterUpdateSuccessful.observe(this) { isSuccess ->
            if (isSuccess) {
                setResult(
                    RESULT_OK,
                    Intent().putExtra(INTENT_CHARACTER, viewModel.character.value)
                )
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            val message = when (error) {
                is AppError.NoInternet -> getString(R.string.error_no_internet)
                is AppError.Failure -> error.msg
                is AppError.SqlError -> getString(R.string.error_sql)
                else -> getString(R.string.default_dialog_message)
            }

            showSimpleDialog(
                context = this,
                title = getString(R.string.default_dialog_title),
                message = message,
                buttonText = getString(R.string.default_dialog_button)
            )
        }
    }

    fun closeActivity() {
        setResult(RESULT_CANCELED)
        finish()
    }

    companion object {
        const val INTENT_CHARACTER = "CHARACTER"
    }
}