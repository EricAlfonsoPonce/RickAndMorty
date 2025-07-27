package es.ericalfonsoponce.presentation.compose.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(

    viewModel: HomeViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()

}

@Composable
private fun HomeScreenContent(){

}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreenContent()
}