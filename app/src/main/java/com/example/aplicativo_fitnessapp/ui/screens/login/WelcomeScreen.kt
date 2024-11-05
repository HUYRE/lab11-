package com.example.aplicativo_fitnessapp.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*

@Composable
fun WelcomeScreen(
    onSignInClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // Colores del degradado
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF101422), // Color superior
            Color(0xFF272B38)  // Color inferior
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush) // fondo degradado
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Distribuye el contenido uniformemente
        ) {
            Text(
                text = "FITNESS APP",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF24C4E6),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            // Animación Lottie centrada
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("Inicio_run.json"))
            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .sizeIn(maxHeight = 300.dp) // Ajuste máximo de altura para la animación
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Botones de Iniciar Sesión y Registrarse
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onSignInClick() },
                    modifier = Modifier
                        .fillMaxWidth(0.7f) // Ajuste al 70% del ancho de la pantalla para adaptabilidad
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF24C4E6)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("Iniciar Sesión", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onRegisterClick() },
                    modifier = Modifier
                        .fillMaxWidth(0.7f) // Ajuste al 70%
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF24C4E6)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("Registrarse", color = Color.Black)
                }
            }
        }
    }
}



