package es.ericalfonsoponce.presentation.xml.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.presentation.xml.R
import es.ericalfonsoponce.presentation.xml.characterDetail.CharacterDetailActivity
import es.ericalfonsoponce.presentation.xml.components.showSimpleDialog
import es.ericalfonsoponce.presentation.xml.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null
    private lateinit var characterAdapter: CharacterAdapter
    private val viewModel: HomeViewModel by viewModels()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.extras?.let { extras ->
                    val characterUpdated =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            extras.getSerializable(
                                CharacterDetailActivity.INTENT_CHARACTER,
                                CharacterShow::class.java
                            )
                        else extras.getSerializable(CharacterDetailActivity.INTENT_CHARACTER) as? CharacterShow

                    val currentList = characterAdapter.currentList
                    val position = currentList.indexOfFirst { it?.id == characterUpdated?.id }
                    if (position != -1) {
                        val newList = currentList.toMutableList().apply {
                            set(position, characterUpdated)
                        }
                        characterAdapter.submitList(newList)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)

        binding?.let {
            WindowInsetsControllerCompat(window, it.root).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }

        setContentView(binding?.root)


        initAdapters()
        initListeners()
        initObservers()
        viewModel.getCharacters()
    }

    private fun initAdapters() {
        characterAdapter = CharacterAdapter(
            onClick = {
                launcher.launch(
                    Intent(this, CharacterDetailActivity::class.java)
                        .putExtra(CharacterDetailActivity.INTENT_CHARACTER, it)
                )
            },
            onDelete = {
                viewModel.deleteCharacter(it)
            },
            loadNextPage = {
                viewModel.loadNextPage()
            }
        )
        binding?.recyclerCharacter?.adapter = characterAdapter
    }

    private fun initListeners() {
        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    private fun initObservers() {
        viewModel.characters.observe(this) {
            characterAdapter.submitList(it)
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                loadShimmerLayout()
            } else {
                stopShimmerLayout()
            }
        }

        viewModel.isLoadingPagination.observe(this) {
            if (it) {
                characterAdapter.showLoader()
            } else {
                characterAdapter.finishPaginationLoader()
            }
        }

        viewModel.error.observe(this) { error ->
            val message = when(error){
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

    private fun loadShimmerLayout() {
        if (binding?.switcherLoaderCharacter?.currentView?.id == R.id.recycler_character) {
            binding?.switcherLoaderCharacter?.showNext()
            binding?.shimmerCharacter?.startShimmer()
        }
        binding?.swipeRefresh?.isRefreshing = true
    }

    private fun stopShimmerLayout() {
        if (binding?.switcherLoaderCharacter?.currentView?.id == R.id.shimmer_character) {
            binding?.switcherLoaderCharacter?.showPrevious()
            binding?.shimmerCharacter?.stopShimmer()
        }
        binding?.swipeRefresh?.isRefreshing = false
    }
}