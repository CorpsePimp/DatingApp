package com.example.datingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.datingapp.presentation.navigation.AppNavGraph
import com.example.datingapp.presentation.ui.theme.DatingAppTheme
import com.example.datingapp.presentation.viewmodel.SettingsViewModel
import com.example.datingapp.utils.LocaleHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(SettingsViewModel::class.java)

        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            val languageCode by settingsViewModel.languageCode.collectAsState()
            val isItMode by settingsViewModel.isItMode.collectAsState()

            val context = LocalContext.current
            LaunchedEffect(languageCode) {
                LocaleHelper.setLocale(context, languageCode)
            }

            DatingAppTheme(darkTheme = isDarkTheme, isItMode = isItMode) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}
