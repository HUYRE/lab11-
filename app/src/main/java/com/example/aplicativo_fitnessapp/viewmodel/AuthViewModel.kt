package com.example.aplicativo_fitnessapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estados de autenticación para observar en la UI
    private val _isLoggedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    private val _currentUser = MutableStateFlow(firebaseAuth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    // Iniciar sesión con correo y contraseña
    fun signInWithEmail(email: String, password: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Inicio de sesión exitoso")
                    _isLoggedIn.value = true
                    _currentUser.value = firebaseAuth.currentUser
                    onResult(true)
                } else {
                    Log.e("AuthViewModel", "Error en el inicio de sesión", task.exception)
                    onResult(false)
                }
            }
    }

    // Registrar un nuevo usuario con correo y contraseña
    fun registerWithEmail(email: String, password: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Registro de usuario exitoso")
                    _isLoggedIn.value = true
                    _currentUser.value = firebaseAuth.currentUser
                    onResult(true)
                } else {
                    Log.e("AuthViewModel", "Error en el registro de usuario", task.exception)
                    onResult(false)
                }
            }
    }

    // Enviar correo de restablecimiento de contraseña
    fun sendPasswordResetEmail(email: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Correo de restablecimiento enviado")
                    onResult(true)
                } else {
                    Log.e("AuthViewModel", "Error al enviar correo de restablecimiento", task.exception)
                    onResult(false)
                }
            }
    }

    // Registrar nombre de usuario después de la creación de cuenta
    fun updateProfile(displayName: String, onResult: (Boolean) -> Unit) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("AuthViewModel", "Nombre de usuario actualizado")
                        _currentUser.value = firebaseAuth.currentUser
                        onResult(true)
                    } else {
                        Log.e("AuthViewModel", "Error al actualizar nombre de usuario", task.exception)
                        onResult(false)
                    }
                }
        }
    }

    // Iniciar sesión con credenciales de Google
    fun signInWithCredential(credential: AuthCredential, onResult: (Boolean) -> Unit) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Inicio de sesión con Google exitoso")
                    _isLoggedIn.value = true
                    _currentUser.value = firebaseAuth.currentUser
                    onResult(true)
                } else {
                    Log.e("AuthViewModel", "Error en el inicio de sesión con Google", task.exception)
                    onResult(false)
                }
            }
    }

    // Cerrar sesión
    fun signOut() {
        firebaseAuth.signOut()
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    // Verificar si el usuario está autenticado al iniciar la app
    init {
        _isLoggedIn.value = firebaseAuth.currentUser != null
        _currentUser.value = firebaseAuth.currentUser
    }
}
