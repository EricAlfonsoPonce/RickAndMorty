package es.ericalfonsoponce.presentation.xml.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import es.ericalfonsoponce.presentation.xml.R
import es.ericalfonsoponce.presentation.xml.databinding.ActivityHomeBinding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null
    private lateinit var characterAdapter: CharacterAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initAdapters()
        initListeners()
        initObservers()
        viewModel.getCharacters()
    }

    private fun initAdapters() {
        characterAdapter = CharacterAdapter(
            onClick = {

            },
            onDelete = {

            },
            loadNextPage = {
                viewModel.getCharacters(isLoadingPagination = true)
            }
        )
        binding?.recyclerCharacter?.adapter = characterAdapter
    }

    private fun initListeners(){
        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    private fun initObservers() {
        binding?.appBar?.addOnOffsetChangedListener { _, verticalOffset ->
            binding?.swipeRefresh?.isEnabled = verticalOffset == 0
        }

        lifecycleScope.launch {
            viewModel.uiState.map { it.characters }
                .distinctUntilChanged()
                .collect { characters ->
                    characterAdapter.submitList(characters)
                }
        }

        lifecycleScope.launch {
            viewModel.uiState.map { it.isLoading }
                .distinctUntilChanged()
                .collect { isLoading ->
                    if (isLoading) {
                        loadShimmerLayout()
                    } else {
                        stopShimmerLayout()
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.uiState.map { it.isLoadingPagination }
                .distinctUntilChanged()
                .collect { isLoading ->
                    if (isLoading) {
                        characterAdapter.showLoader()
                    } else {
                        characterAdapter.finishPaginationLoader()
                    }
                }
        }
    }

    private fun loadShimmerLayout() {
        if (binding?.switcherLoaderCharacter?.currentView?.id == R.id.recycler_character) {
            binding?.switcherLoaderCharacter?.showNext()
            binding?.shimmerCharacter?.startShimmer()
        }
        binding?.swipeRefresh?.isEnabled = false
    }

    private fun stopShimmerLayout() {
        if (binding?.switcherLoaderCharacter?.currentView?.id == R.id.shimmer_character) {
            binding?.switcherLoaderCharacter?.showPrevious()
            binding?.shimmerCharacter?.stopShimmer()
        }
        binding?.swipeRefresh?.isEnabled = true
    }
}