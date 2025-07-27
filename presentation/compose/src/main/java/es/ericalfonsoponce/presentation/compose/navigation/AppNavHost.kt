package es.ericalfonsoponce.presentation.compose.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.presentation.compose.navigation.AppNavHost.CHARACTER_KEY
import es.ericalfonsoponce.presentation.compose.screens.characterDetail.CharacterDetailScreen
import es.ericalfonsoponce.presentation.compose.screens.home.HomeScreen

private object AppNavHost{
    const val CHARACTER_KEY = "CHARACTER"
}

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val navigateBack: () -> Unit = { navController.navigateUp() }

    val navigateToCharacterDetail: (CharacterShow) -> Unit = {
        navController.navigate(Screens.CharacterDetailScreen(it))
    }

    val navigateToHomeScreen: (CharacterShow) -> Unit = {
        navController.popBackStack()
        navController.currentBackStackEntry?.savedStateHandle?.set(CHARACTER_KEY, it)
    }

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                initialOffsetX = { it }
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                initialOffsetX = { -it }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                targetOffsetX = { -it }
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                targetOffsetX = { it }
            )
        }
    ){
        composable<Screens.HomeScreen>{ navBackStackEntry ->
            val character: CharacterShow? = navBackStackEntry.savedStateHandle.remove<CharacterShow>(CHARACTER_KEY)

            HomeScreen(
                character = character,
                navigateToCharacterDetail = navigateToCharacterDetail
            )
        }

        composable<Screens.CharacterDetailScreen>(typeMap = Screens.CharacterDetailScreen.typeMap){
            CharacterDetailScreen(
                navigateBack = navigateBack,
                navigateToHomeScreen = navigateToHomeScreen
            )
        }
    }
}