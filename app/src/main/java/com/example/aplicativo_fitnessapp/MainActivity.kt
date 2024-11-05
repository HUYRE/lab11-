package com.example.aplicativo_fitnessapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.aplicativo_fitnessapp.ui.screens.login.HomeScreen
import com.example.aplicativo_fitnessapp.ui.screens.login.LoginScreen
import com.example.aplicativo_fitnessapp.ui.screens.login.RegisterScreen
import com.example.aplicativo_fitnessapp.ui.screens.login.WelcomeScreen
import com.example.aplicativo_fitnessapp.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicialización de Places SDK Google Maps
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyDBBHn5VHObbgUU5_5WR75ZBJ8XeS9Cdlg")
        }

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            // Variables para controlar la navegación entre pantallas
            var showRegisterScreen by remember { mutableStateOf(false) }
            var showWelcomeScreen by remember { mutableStateOf(true) }
            val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = false)

            when {
                // Pantalla de bienvenida
                showWelcomeScreen -> {
                    WelcomeScreen(
                        onSignInClick = {
                            showWelcomeScreen = false // Navega a la pantalla de inicio de sesión
                        },
                        onRegisterClick = {
                            showRegisterScreen = true // Navega a la pantalla de registro
                            showWelcomeScreen = false // Oculta la pantalla de bienvenida
                        }
                    )
                }
                // Pantalla de inicio de sesión o de registro
                isLoggedIn -> {
                    HomeScreen(// ser
                        authViewModel = authViewModel,
                        onLogout = { authViewModel.signOut() }
                    )
                }
                showRegisterScreen -> {
                    RegisterScreen(
                        authViewModel = authViewModel,
                        onRegistrationSuccess = {
                            // Al registrarse exitosamente, vuelve a la pantalla de inicio de sesión
                            showRegisterScreen = false
                        }
                    )
                }
                else -> {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onGoogleSignIn = { signInWithGoogle() },
                        onNavigateToRegister = { showRegisterScreen = true },
                        onLoginSuccess = {
                            // Navega a HomeScreen después de un inicio de sesión exitoso
                            showWelcomeScreen = false
                        }
                    )
                }
            }
        }
    }

    // Función para manejar el inicio de sesión con Google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
    }

    // Manejo del resultado de Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    authViewModel.signInWithCredential(credential) { success ->
                        if (success) {
                            // Maneja el éxito del inicio de sesión
                        }
                    }
                }
            } catch (e: ApiException) {
                // Maneja el error de inicio de sesión
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 1001
    }
}
