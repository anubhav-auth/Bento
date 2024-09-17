package com.anubhav_auth.bento.userInterface

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anubhav_auth.bento.database.LocalDatabaseViewModel
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
    val savedAddresses by localDatabaseViewModel.savedAddress.collectAsState()
    val context = LocalContext.current
    val interactionSource = remember {
        MutableInteractionSource()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val a = getFromLocationShared(context)
                Log.d("mytag", a.toString())
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
    }
}