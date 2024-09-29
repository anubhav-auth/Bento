package com.anubhav_auth.bento.userInterface

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.anubhav_auth.bento.entities.backendRecieved.menuEntity.MenuItem
import com.anubhav_auth.bento.ui.theme.MyFonts
import com.anubhav_auth.bento.viewmodels.BentoViewModel
import com.anubhav_auth.bento.viewmodels.SharedStateViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MenuPage(
    bentoViewModel: BentoViewModel,
    sharedStateViewModel: SharedStateViewModel,
    scope: CoroutineScope,
    restroId: String,
    navController: NavController
) {
    LaunchedEffect(key1 = sharedStateViewModel.cartItems) {
        Log.d("mytag", sharedStateViewModel.cartItems.toString())
    }

//    LaunchedEffect(restroId) {
    bentoViewModel.loadRestaurantById(restroId)
    bentoViewModel.loadMenuData(restroId)
//    }


    val restroDetails by bentoViewModel.restaurantData.collectAsState()
    val menuItems by bentoViewModel.menuData.collectAsState()


    Scaffold(
        floatingActionButton = {
            if (sharedStateViewModel.cartItems.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("cartPage")
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = "")
                }
            }
        }
    ) { _ ->
        if (restroDetails == null) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                item {
                    AsyncImage(
                        model = restroDetails!!.imageUrl, contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    Text(
                        text = restroDetails!!.name,
                        fontSize = 24.sp,
                        fontFamily = MyFonts.openSansBold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                items(menuItems) { item ->
                    MenuItem(item = item, sharedStateViewModel = sharedStateViewModel)
                }

            }
        }
    }


}

@Composable
fun MenuItem(item: MenuItem, sharedStateViewModel: SharedStateViewModel) {
    val ctx = LocalContext.current
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 21.dp)
            .fillMaxWidth()
            .clickable { }
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.7f)) {
            Text(
                text = item.name,
                modifier = Modifier.padding(12.dp),
                fontSize = 18.sp,
                fontFamily = MyFonts.roboto_regular,
                fontWeight = FontWeight.W400
            )
            Text(
                text = item.description,
                modifier = Modifier.padding(12.dp),
                fontSize = 15.sp,
                fontFamily = MyFonts.roboto_regular,
                fontWeight = FontWeight.W400
            )
        }
        Column {
            AsyncImage(
                model = item.imageUrl, contentDescription = "", modifier = Modifier
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .size(100.dp)
            )
            Button(
                onClick = {
                    sharedStateViewModel.cartItems.add(item.id)
                }
            ) {
                Text(text = "add")
            }
        }
    }
}