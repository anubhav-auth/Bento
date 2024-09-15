package com.anubhav_auth.bento.location

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anubhav_auth.bento.BentoViewModel
import com.anubhav_auth.bento.R
import com.anubhav_auth.bento.SharedStateViewModel
import com.anubhav_auth.bento.database.LocalDatabaseViewModel
import com.anubhav_auth.bento.database.entities.AddressTypes
import com.anubhav_auth.bento.database.entities.SavedAddress
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerLocation(
    scope: CoroutineScope,
    locationViewmodel: LocationViewmodel,
    sharedStateViewModel: SharedStateViewModel,
    localDatabaseViewModel: LocalDatabaseViewModel,
    bentoViewModel: BentoViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient,
    navController: NavController
) {

    val context = LocalContext.current
    locationViewmodel.getLatLang(fusedLocationProviderClient, true)
    val locationFromSearch by sharedStateViewModel.selectedAddress

    val currentLocation by locationViewmodel.latLang.collectAsState()
    var centerLocation =
        if (locationFromSearch != null) locationFromSearch!!.geometry.location.let {
            LatLng(it.lat, it.lng)
        } else {
            currentLocation
        }

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
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            val focusRequester = List(6) { FocusRequester() }

            var houseNumber by remember { mutableStateOf("") }
            var floorNumber by remember { mutableStateOf("") }
            var buildingName by remember { mutableStateOf("") }
            var howToReach by remember { mutableStateOf("") }
            var contactNumber by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }

            val sheetCameraPosition = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(centerLocation, 19.5f)
            }
            geoCodeData?.results?.get(1)?.let { geo ->
                LazyColumn(
                    horizontalAlignment = Alignment.Start, modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 13.dp)
                ) {
                    item {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier
                                .size(27.dp)
                                .clickable {
                                    scope.launch {
                                        scaffoldState.bottomSheetState.hide()
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Add Address Details",
                            fontSize = 21.sp,
                            fontFamily = MyFonts.montserrat_semi_bold
                        )
                        Spacer(modifier = Modifier.height(9.dp))
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
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = geo.address_components[0].long_name,
                            fontSize = 21.sp,
                            fontFamily = MyFonts.openSansBold
                        )
                        Spacer(modifier = Modifier.height(9.dp))
                        Text(
                            text = geo.formatted_address,
                            fontSize = 15.sp,
                            fontFamily = MyFonts.lato_regular
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        AddressComponentItem(
                            heading = "House / Flat Number",
                            text = houseNumber,
                            valChange = { houseNumber = it },
                            index = 0,
                            focusRequesters = focusRequester,
                            imeAction = ImeAction.Next
                        )
                        AddressComponentItem(
                            text = floorNumber,
                            valChange = { floorNumber = it },
                            heading = "Floor number",
                            index = 1,
                            focusRequesters = focusRequester,
                            imeAction = ImeAction.Next
                        )
                        AddressComponentItem(
                            text = buildingName,
                            valChange = { buildingName = it },
                            heading = "Apartment/ Building name",
                            index = 2,
                            focusRequesters = focusRequester,
                            imeAction = ImeAction.Next
                        )
                        AddressComponentItem(
                            text = howToReach,
                            valChange = { howToReach = it },
                            heading = "How to reach",
                            singleLine = false,
                            maxLines = 5,
                            index = 3,
                            focusRequesters = focusRequester,
                            imeAction = ImeAction.Default
                        )
                        AddressComponentItem(
                            text = contactNumber,
                            valChange = { contactNumber = it },
                            heading = "Contact number",
                            isContact = true,
                            index = 4,
                            focusRequesters = focusRequester,
                            imeAction = ImeAction.Next
                        )
                        AddressComponentItem(
                            heading = "Name",
                            text = name,
                            valChange = { name = it },
                            index = 5,
                            focusRequesters = focusRequester,
                            imeAction = ImeAction.Done
                        )
                        AddressDynamicChipMenu(
                            chipText = AddressTypes.AsStringList(),
                            sharedStateViewModel = sharedStateViewModel
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp),
                            onClick = {
                                val address = SavedAddress(
                                    name = name,
                                    houseFlatNo = houseNumber,
                                    floorNo = floorNumber,
                                    apartmentBuildingName = buildingName,
                                    howToReach = howToReach,
                                    contactNumber = contactNumber,
                                    latLng = centerLocation,
                                    mapAddress = geo.formatted_address,
                                    addressType = AddressTypes.entries[sharedStateViewModel.selectedAddressIndex.value]
                                )
                                localDatabaseViewModel.saveAddress(
                                    address
                                )
                                scope.launch {
                                    scaffoldState.bottomSheetState.hide()
                                }
                                navController.navigateUp()
                            },
                            shape = RoundedCornerShape(9.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ) {
                            Text(text = "Save Address")
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
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
                    scrollGesturesEnabledDuringRotateOrZoom = false,

                    ),
                properties = MapProperties(
                    isBuildingEnabled = true,
                    isIndoorEnabled = true,
                    mapType = MapType.NORMAL

                ),
                onMapLoaded = {
                    centerLocation.let {
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(it, 19.5f)
                    }
                },
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
                    .clip(RoundedCornerShape(12.dp))
                    .border(width = 2.dp, Color.LightGray, shape = RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
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
                                fontFamily = MyFonts.lato_regular,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(15.dp)
                        .fillMaxWidth()
                        .height(45.dp),
                    enabled = geoCodeData?.results?.get(1) != null,
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    },
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )

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
    text: String,
    valChange: (String) -> Unit,
    heading: String,
    isContact: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    index: Int,
    focusRequesters: List<FocusRequester>,
    imeAction: ImeAction,
) {
    var text2 = text
    val phoneNumberRegex = Regex("[^0-9]")
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(9.dp))
        Text(text = heading, fontWeight = FontWeight.SemiBold, fontFamily = MyFonts.lato_regular)
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = text,
            onValueChange = {
                if (isContact) {
                    val stripped = phoneNumberRegex.replace(it, "")
                    valChange(if (stripped.length >= 10) stripped.substring(0..9) else stripped)
                } else {
                    valChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))
                .focusRequester(focusRequesters[index]),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isContact) KeyboardType.Number else KeyboardType.Text,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequesters[index + 1].requestFocus()
                }
            ),
            prefix = { if (isContact) Text(text = "+91") }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddressDynamicChipMenu(chipText: List<String>, sharedStateViewModel: SharedStateViewModel) {
    FlowRow(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize()
    ) {
        chipText.forEachIndexed { index, text ->
            AddressChipItem(text, index, sharedStateViewModel)
        }
    }
}

@Composable
fun AddressChipItem(text: String, index: Int, sharedStateViewModel: SharedStateViewModel) {
    Box(
        modifier = Modifier
            .padding(start = 9.dp, top = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable {
                sharedStateViewModel.selectedAddressIndex.value = index
            }
            .border(
                1.dp, if (index == sharedStateViewModel.selectedAddressIndex.value) Color.Green else
                    Color.Gray, RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)

    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 12.sp,
            fontFamily = MyFonts.openSansBold,
            fontWeight = FontWeight.Bold
        )
    }
}
