package com.example.aplicativo_fitnessapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(context: Context) {
    // Inicializar Places si aún no está inicializado
    if (!Places.isInitialized()) {
        Places.initialize(context, "AIzaSyDBBHn5VHObbgUU5_5WR75ZBJ8XeS9Cdlg") // Reemplaza "YOUR_API_KEY" por tu clave API real
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val placesClient: PlacesClient = Places.createClient(context)
    val coroutineScope = rememberCoroutineScope()

    var location by remember { mutableStateOf<LatLng?>(null) }
    var gymLocations by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // Configuración para solicitar permisos de ubicación
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                coroutineScope.launch {
                    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                        if (loc != null) {
                            location = LatLng(loc.latitude, loc.longitude)
                            fetchNearbyGyms(placesClient, location) { gyms ->
                                gymLocations = gyms
                            }
                        }
                    }
                }
            } else {
                // Manejar la denegación del permiso
            }
        }
    )

    // Solicitar el permiso cuando se monta el Composable
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    location?.let { currentLocation ->
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentLocation, 20f) // Ajusta el zoom según tus necesidades
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Marcador para la ubicación actual del usuario (color azul)
            Marker(
                state = rememberMarkerState(position = currentLocation),
                title = "Tu ubicación",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            )

            // Marcadores para cada gimnasio encontrado (color rojo)
            gymLocations.forEach { gymLocation ->
                Marker(
                    state = rememberMarkerState(position = gymLocation),
                    title = "Gimnasio cercano",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }
    }
}

// Función para buscar gimnasios cercanos usando Google Places
@SuppressLint("MissingPermission")
private fun fetchNearbyGyms(
    placesClient: PlacesClient,
    currentLocation: LatLng?,
    onGymsFetched: (List<LatLng>) -> Unit
) {
    if (currentLocation == null) return

    val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)
    val request = FindCurrentPlaceRequest.newInstance(placeFields)

    placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
        // Filtrar solo lugares de tipo "gimnasio" y obtener sus coordenadas
        val gyms = response.placeLikelihoods
            .filter { it.place.types?.contains(Place.Type.GYM) == true }
            .mapNotNull { it.place.latLng }

        onGymsFetched(gyms)
    }.addOnFailureListener { exception ->
        // Manejar errores al buscar gimnasios
        exception.printStackTrace()
    }
}
