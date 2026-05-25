package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.TravelAppMainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.TravelViewModel
import com.example.ui.viewmodel.TravelViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Instantiating our flagship travel view model passing application context
                val viewModel: TravelViewModel = viewModel(
                    factory = TravelViewModelFactory(application)
                )
                TravelAppMainScreen(viewModel = viewModel)
            }
        }
    }
}
