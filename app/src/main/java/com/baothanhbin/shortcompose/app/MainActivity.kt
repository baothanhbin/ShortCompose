package com.baothanhbin.shortcompose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.baothanhbin.shortcompose.app.ui.AppUiProviders
import com.baothanhbin.shortcompose.app.ui.MainApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppUiProviders {
                MainApp()
            }
        }
    }
}
