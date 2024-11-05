package com.example.aplicativo_fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.libraries.places.api.Places

class MapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Places si aún no está inicializado
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyDBBHn5VHObbgUU5_5WR75ZBJ8XeS9Cdlg")
        }

        setContent {
            MapScreen(context = this) // Llama a tu MapScreen
        }
    }
}
