package com.example.aplicativo_fitnessapp.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicativo_fitnessapp.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Bienvenido a la App de Fitness!",
            fontSize = 24.sp,
            color = Color(0xFF1E88E5) // Color primario
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                authViewModel.signOut() // Cerrar sesión usando el ViewModel
                onLogout() // Navegar de vuelta a la pantalla de login
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E88E5) // Color primario para el botón
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Cerrar Sesión", color = Color.White)
        }
    }
}
