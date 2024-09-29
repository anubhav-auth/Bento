package com.anubhav_auth.bento.userInterface

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.anubhav_auth.bento.database.LocalDatabaseViewModel
import com.anubhav_auth.bento.entities.backendRecieved.menuEntity.MenuItem
import com.anubhav_auth.bento.location.getFromLocationShared
import com.anubhav_auth.bento.ui.theme.MyFonts
import com.anubhav_auth.bento.viewmodels.BentoViewModel
import com.anubhav_auth.bento.viewmodels.SharedStateViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CartPage(
    sharedStateViewModel: SharedStateViewModel,
    bentoViewModel: BentoViewModel,
    localDatabaseViewModel: LocalDatabaseViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val cartItemIds: List<String> = sharedStateViewModel.cartItems
    bentoViewModel.loadCartData(cartItemIds)
    val cartItems by bentoViewModel.cartData.collectAsState()
    val savedAddress by localDatabaseViewModel.savedAddress.collectAsState()
    val a = getFromLocationShared(context)

    LazyColumn(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
        item {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        navController.navigateUp()
                    }
                    .size(33.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Cart", fontSize = 27.sp, fontFamily = MyFonts.openSansBold)
            Spacer(modifier = Modifier.height(21.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "",
                    modifier = Modifier
                        .size(27.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text(
                        text = "Delivery Address",
                        fontSize = 18.sp,
                        fontFamily = MyFonts.openSansBold
                    )
                    Spacer(modifier = Modifier.height(9.dp))
                    Text(
                        text = if (a > -1) {
                            savedAddress.find { it.id == a }?.mapAddress.toString()
                        } else {
                            "Choose Location"
                        },
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(21.dp))
            HorizontalDivider(thickness = 3.dp)
            Spacer(modifier = Modifier.height(21.dp))
            Text(text = "Items in your Cart", fontSize = 21.sp, fontFamily = MyFonts.openSansBold)
            Spacer(modifier = Modifier.height(15.dp))
        }
        items(cartItems) { cartItem ->
            CartItem(item = cartItem)
        }
        item {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(21.dp))
            PaymentSummary(items = cartItems)
            Spacer(modifier = Modifier.height(21.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                onClick = {

                },
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {

                Text(text = "Make Payment", fontSize = 18.sp)

            }
        }
    }
}

@Composable
fun CartItem(item: MenuItem) {
    Row(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 21.dp)
            .fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl, contentDescription = "", modifier = Modifier
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .size(75.dp)
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = item.name,
                modifier = Modifier.padding(12.dp),
                fontSize = 18.sp,
                fontFamily = MyFonts.roboto_regular,
                fontWeight = FontWeight.W400
            )
            Text(
                text = "₹${item.price}",
                modifier = Modifier.padding(12.dp),
                fontSize = 15.sp,
                fontFamily = MyFonts.roboto_regular,
                fontWeight = FontWeight.W400
            )
        }
    }
}

fun Int.percentage(aj: Double): Double {
    return (aj / 100) * this.toDouble()
}

fun Double.round(places: Int): Double {
    return BigDecimal(this).setScale(places, RoundingMode.HALF_EVEN).toDouble()
}

@Composable
fun PaymentSummary(items: List<MenuItem>) {
    var a = 0
    items.forEach {
        a += it.price
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Bill Details",
            fontSize = 21.sp,
            fontFamily = MyFonts.montserrat_semi_bold,
            modifier = Modifier.padding(vertical = 6.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(text = "SubTotal:")
            Text(text = "₹ $a")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(text = "Delivery Charges:")
            Text(text = "₹ ${a.percentage(2.0).round(2)}")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(text = "Tax:")
            Text(text = "₹ ${a.percentage(5.0).round(2)}")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(text = "Total:", fontSize = 21.sp, fontFamily = MyFonts.montserrat_semi_bold)
            Text(
                text = "₹ ${a + a.percentage(5.0) + a.percentage(2.0).round(2)}",
                fontSize = 18.sp,
                fontFamily = MyFonts.montserrat_semi_bold
            )
        }
    }
}