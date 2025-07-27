package es.ericalfonsoponce.presentation.compose.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import es.ericalfonsoponce.presentation.compose.navigation.AppNavHost

@AndroidEntryPoint
class MainComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val view = LocalView.current
            val context = LocalContext.current

            SideEffect {
                val window = (context as Activity).window

                WindowInsetsControllerCompat(window, view).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }

            val navController = rememberNavController()
            AppNavHost(navController)
        }
    }
}