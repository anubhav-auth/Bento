package com.anubhav_auth.bento.location

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anubhav_auth.bento.BentoViewModel
import com.anubhav_auth.bento.R
import com.anubhav_auth.bento.ui.theme.MyFonts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerLocation(
    locationViewmodel: LocationViewmodel,
    bentoViewModel: BentoViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    val context = LocalContext.current
    locationViewmodel.getLatLang(fusedLocationProviderClient, true)

    val currentLocation by locationViewmodel.latLang.collectAsState()
    var centerLocation = currentLocation
    LaunchedEffect(Unit) {
        bentoViewModel.loadPLaceDataFromLatLang(centerLocation)
    }
    val geoCodeData by bentoViewModel.geocodeData.collectAsState()


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }

    LaunchedEffect(locationViewmodel.showErrorChannel) {
        locationViewmodel.showErrorChannel.collectLatest { show ->
            if (show) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            centerLocation = cameraPositionState.position.target

            centerLocation.let { location ->
                bentoViewModel.loadPLaceDataFromLatLang(location)
            }
        }
    }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
            skipHiddenState = false
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            val sheetCameraPosition = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(centerLocation, 19.5f)
            }
            geoCodeData?.results?.get(1)?.let { geo ->
                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(12.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    Text(text = "Add Address Details")
                    GoogleMap(
                        cameraPositionState = sheetCameraPosition,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        uiSettings = MapUiSettings(
                            compassEnabled = false,
                            zoomControlsEnabled = false,
                            rotationGesturesEnabled = false,
                            scrollGesturesEnabled = false,
                            tiltGesturesEnabled = false,
                            zoomGesturesEnabled = false
                        )
                    ) {
                        Marker(
                            state = rememberMarkerState(position = centerLocation)
                        )
                    }
                    Text(text = geo.address_components[0].long_name)
                    Text(text = geo.formatted_address)
                    AddressComponentItem(heading = "House / Flat Number")
                    AddressComponentItem(heading = "Floor number")
                    AddressComponentItem(heading = "Apartment/ Building name")
                    AddressComponentItem(heading = "How to reach")
                    AddressComponentItem(heading = "Contact number")
                    AddressComponentItem(heading = "Name")
                }
            }
        },
        sheetDragHandle = {},
        sheetPeekHeight = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    tiltGesturesEnabled = false,
                    scrollGesturesEnabledDuringRotateOrZoom = false
                ),
                properties = MapProperties(
                    isBuildingEnabled = true,
                    isIndoorEnabled = true,
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
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .fillMaxWidth()
                    .fillMaxHeight(0.18f)
            ) {

                geoCodeData?.results?.get(1)?.let {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "",
                            modifier = Modifier.size(27.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = it.address_components[0].long_name,
                                fontSize = 21.sp,
                                fontFamily = MyFonts.openSansBold
                            )
                            Spacer(modifier = Modifier.height(9.dp))
                            Text(
                                text = it.formatted_address,
                                fontSize = 15.sp,
                                fontFamily = MyFonts.lato_regular
                            )
                        }
                    }
                }

                Button(
                    onClick = { }
                ) {
                    Text(text = "Confirm Location")
                }
            }

        }
    }
}

@Composable
fun AddressComponentItem(
    modifier: Modifier = Modifier,
    heading: String,
    isContact: Boolean = false,
    singleLine: Boolean = true,
    maxLines:Int = 1
) {


    var text by remember {
        mutableStateOf("")
    }

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(9.dp))
        Text(text = heading, fontWeight = FontWeight.SemiBold, fontFamily = MyFonts.lato_regular)
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = singleLine,
            maxLines = maxLines,
           keyboardOptions =  KeyboardOptions(
               keyboardType = if (isContact)KeyboardType.Number else KeyboardType.Text
           )
        )
    }
}
