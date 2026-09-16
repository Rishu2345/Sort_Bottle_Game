package com.buildsol.bottolshort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.buildsol.bottolshort.ui.navigation.BottleSortNavHost
import com.buildsol.bottolshort.ui.theme.BottolShortTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BottolShortTheme {
                BottleSortNavHost()
            }
        }
    }
}
