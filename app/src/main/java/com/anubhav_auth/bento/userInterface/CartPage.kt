package com.anubhav_auth.bento.userInterface

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.anubhav_auth.bento.viewmodels.BentoViewModel
import com.anubhav_auth.bento.viewmodels.SharedStateViewModel

@Composable
fun CartPage(sharedStateViewModel: SharedStateViewModel, bentoViewModel: BentoViewModel) {
    val cartItemIds: List<String> = sharedStateViewModel.cartItems
    bentoViewModel.loadCartData(cartItemIds)
    val cartItems by bentoViewModel.cartData.collectAsState()
    
    LazyColumn {
        items(cartItems){
            Text(text = it.name)
        }
    }
}