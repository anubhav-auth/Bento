package com.anubhav_auth.bento

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.anubhav_auth.bento.location.LocationViewmodel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MarkerLocation(locationViewmodel: LocationViewmodel, fusedLocationProviderClient: FusedLocationProviderClient) {
    val context = LocalContext.current
    locationViewmodel.getLatLang(fusedLocationProviderClient, true)

    val currentLocation by locationViewmodel.latLang.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }

    LaunchedEffect(locationViewmodel.showErrorChannel) {
        locationViewmodel.showErrorChannel.collectLatest { show->
            if (show){
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val centerLocation = cameraPositionState.position.target

            centerLocation.let {location->
                Log.d("mytag",centerLocation.toString())
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
            ),
            properties = MapProperties(
                isBuildingEnabled = true,
                mapType = MapType.NORMAL
            ),
            onMapLoaded = {
                currentLocation?.let {
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(it, 19.5f)
                }
            }
        )
        Image(
            painter = painterResource(id = R.drawable.marker_image),
            contentDescription = "Center Marker",
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp)
        )
    }
}