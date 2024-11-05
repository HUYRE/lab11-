package com.example.aplicativo_fitnessapp.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicativo_fitnessapp.viewmodel.AuthViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.aplicativo_fitnessapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onGoogleSignIn: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showPasswordResetMessage by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Colores personalizados
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF101422), // Color superior
            Color(0xFF272B38)  // Color inferior
        )
    )
    val textColor = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FITNESS APP",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF24C4E6),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Image(
            painter = painterResource(R.drawable.logo_fit_x),
            contentDescription = "Logo",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF588BB8),
                cursorColor = textColor,
                focusedIndicatorColor = textColor,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = textColor,
                unfocusedLabelColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF588BB8),
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray
            ),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(color = textColor)
        } else {
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        authViewModel.signInWithEmail(email, password) { success ->
                            isLoading = false
                            if (success) {
                                onLoginSuccess()
                            } else {
                                errorMessage = "Error al iniciar sesión. Por favor, verifica tus credenciales."
                            }
                        }
                    } else {
                        errorMessage = "Por favor, completa todos los campos."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF35B4CF)
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Iniciar Sesión", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de inicio de sesión con Google
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onGoogleSignIn() },
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.google_icon),
                    contentDescription = "Iniciar sesión con Google",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                if (email.isNotEmpty()) {
                    authViewModel.sendPasswordResetEmail(email) { success ->
                        if (success) {
                            showPasswordResetMessage = true
                            errorMessage = ""
                        } else {
                            errorMessage = "No se pudo enviar el correo de recuperación. Intenta de nuevo."
                            showPasswordResetMessage = false
                        }
                    }
                } else {
                    errorMessage = "Introduce tu correo para restablecer la contraseña."
                }
            }
        ) {
            Text("¿Olvidaste tu contraseña?", color = Color.White)
        }

        if (showPasswordResetMessage) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Correo de recuperación enviado.", color = Color.White)
        }
    }
}

