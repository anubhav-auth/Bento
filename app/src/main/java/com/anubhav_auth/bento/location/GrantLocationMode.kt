package com.anubhav_auth.bento.location

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anubhav_auth.bento.BentoViewModel
import com.anubhav_auth.bento.R
import com.anubhav_auth.bento.SharedStateViewModel
import com.anubhav_auth.bento.database.entities.placesData.PlacesData
import com.anubhav_auth.bento.database.entities.placesData.Result
import com.anubhav_auth.bento.ui.theme.MyFonts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrantLocationMode(
    sharedStateViewModel: SharedStateViewModel,
    bentoViewModel: BentoViewModel,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    scope: CoroutineScope
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            SheetSearch(
                scope = scope,
                scaffoldState = scaffoldState,
                bentoViewModel = bentoViewModel
            )
        },
        sheetPeekHeight = 0.dp,
        sheetDragHandle = {}
    ) {
        LocationChoice(
            sharedStateViewModel = sharedStateViewModel,
            requestPermissionLauncher = requestPermissionLauncher,
            scaffoldState = scaffoldState,
            scope = scope
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationChoice(
    sharedStateViewModel: SharedStateViewModel,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {
    val permissionArray = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val permissionGranted by sharedStateViewModel.allPermissionGranted


    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "",
            modifier = Modifier
                .size(320.dp)
                .rotate(-90f)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Grant Current Location",
                modifier = Modifier.width(240.dp),
                fontFamily = MyFonts.montserrat_semi_bold,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))



            Text(
                text = "This lets us show nearby restaurants you can order from",
                modifier = Modifier.width(240.dp),
                fontFamily = MyFonts.lato_regular,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                onClick = {
                    if (!permissionGranted) {
                        requestPermissionLauncher.launch(
                            permissionArray
                        )
                    }
                },
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, contentColor = Color.White
                )
            ) {
                Text(text = "Use current location", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF8F8FF), contentColor = Color.Black
                )
            ) {
                Text(text = "Enter Manually", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetSearch(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    bentoViewModel: BentoViewModel
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val placesData by bentoViewModel.placesData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp), horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "",
            modifier = Modifier
                .clickable {
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                }
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Search Location",
            fontSize = 24.sp,
            fontFamily = MyFonts.openSansBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = searchText,
            onValueChange = { searchParam ->
                searchText = searchParam
                if (searchParam.isEmpty()) {
                    bentoViewModel.clearLoadedPlacesDate()
                } else {
                    bentoViewModel.loadPlacesData(searchText)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = "eg. BTM Layout") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "search icon"
                )
            },
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(21.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = Color.LightGray.copy(alpha = 0.55f),
                unfocusedContainerColor = Color.LightGray.copy(alpha = 0.55f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )

        )
        Spacer(modifier = Modifier.height(24.dp))
        HistoryMenu()
//        SearchResultMenu(placesData = placesData, modifier = Modifier.align(Alignment.Start))
    }
}

@Composable
fun SearchResultMenu(placesData: PlacesData?, modifier: Modifier = Modifier) {
    placesData?.results?.let { placesResults ->

        LazyColumn {
            items(placesResults) { place ->
                SearchResultItem(result = place, modifier = modifier)
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}

@Composable
fun SearchResultItem(result: Result?, modifier: Modifier = Modifier) {
    result?.let {
        Row(modifier = modifier.padding(bottom = 24.dp)) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "location",
                modifier = Modifier.size(30.dp)
            )
            Column {
                Text(text = it.name, fontSize = 18.sp, fontFamily = MyFonts.montserrat_semi_bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = result.formatted_address,
                    fontFamily = MyFonts.roboto_regular,
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

@Composable
fun HistoryMenu() {
    // TODO: click to use current location 
    Column {
        HistoryMenuItem(
            historyItemContent = HistoryItemContent(
                icon = R.drawable.target,
                title = "Use Current Location"
            ),
            modifier = Modifier.clickable {
                
            }
        )
        HorizontalDivider()
        Text(text = "Recent Searches")
        HorizontalDivider()
        Text(text = "Saved Addresses")
    }
}


@Composable
fun HistoryMenuItem(historyItemContent: HistoryItemContent, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = historyItemContent.icon), contentDescription = "", modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = historyItemContent.title, fontSize = 15.sp, fontWeight = FontWeight.W500)
            if (historyItemContent.description != null) {
                Text(text = historyItemContent.description)
            } 
        }
    }
}

data class HistoryItemContent(
    val icon: Int,
    val title: String,
    val description: String? = null,
)


