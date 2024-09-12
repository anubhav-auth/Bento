package com.anubhav_auth.bento.location

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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
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

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Start)
                .clickable {
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                }
        )
        Text(text = "Search Location")
        TextField(value = searchText, onValueChange = { searchParam ->
            searchText = searchParam
            bentoViewModel.loadPlacesData(searchText)
        })
        SearchResultMenu(placesData = placesData)
    }
}

@Composable
fun SearchResultMenu(placesData: PlacesData?) {
    placesData?.results?.let { placesResults ->

        LazyColumn {
            items(placesResults) { place ->
                SearchResultItem(result = place)
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}

@Composable
fun SearchResultItem(result: Result?) {
    result?.let {
        Row {
            Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "location")
            Column {
                Text(text = it.name)
                Text(text = result.formatted_address)

            }
        }
    }
}

@Preview
@Composable
private fun pre() {
    SearchResultItem(result = )
}

