package com.anubhav_auth.bento.userInterface

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.anubhav_auth.bento.database.LocalDatabaseViewModel
import com.anubhav_auth.bento.entities.backendRecieved.restaurantEntity.RestaurantEntityItem
import com.anubhav_auth.bento.location.getFromLocationShared
import com.anubhav_auth.bento.ui.theme.MyFonts
import com.anubhav_auth.bento.viewmodels.BentoViewModel
import com.anubhav_auth.bento.viewmodels.SharedStateViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    sharedStateViewModel: SharedStateViewModel,
    localDatabaseViewModel: LocalDatabaseViewModel,
    scope: CoroutineScope,
    bentoViewModel: BentoViewModel,
    navController: NavController
) {
    localDatabaseViewModel.getSavedAddresses()
    bentoViewModel.loadAllRestaurantData()

    val savedAddresses by localDatabaseViewModel.savedAddress.collectAsState()

    val allRestaurants by bentoViewModel.allRestaurantData.collectAsState()

    val context = LocalContext.current
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Log.d("mytag", allRestaurants.toString())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val a = getFromLocationShared(context)
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(0.45f)
                        .height(50.dp)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            navController.navigate("sheetSearch")
                        }
                ) {
                    Column {
                        Text(text = "Deliver Here", fontFamily = MyFonts.lato_regular)
                        Text(
                            text = if (a > -1) {
                                savedAddresses.find { it.id == a }?.mapAddress.toString()
                            } else {
                                "Choose Location"
                            },
                            fontSize = 18.sp,
                            fontFamily = MyFonts.openSansBold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
                Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "")

            }
        }
        items(allRestaurants) { restaurant ->
            RestaurantItem(restaurantEntityItem = restaurant, bentoViewModel = bentoViewModel)
        }

    }
}

@Composable
fun RestaurantItem(restaurantEntityItem: RestaurantEntityItem, bentoViewModel: BentoViewModel) {
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 21.dp)
            .fillMaxWidth()
            .clickable {  }
    ) {
        AsyncImage(
            model = restaurantEntityItem.imageUrl,
            contentDescription = "",
            modifier = Modifier
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(Color.Red)
                .height(75.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = restaurantEntityItem.name,
                fontSize = 18.sp,
                fontFamily = MyFonts.openSansBold,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier
                )
                Text(
                    text = restaurantEntityItem.rating.toString(),
                    fontSize = 12.sp,
                    fontFamily = MyFonts.openSansBold,
                    modifier = Modifier
                )
            }

        }

    }
}
