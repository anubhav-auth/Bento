package com.anubhav_auth.bento.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.anubhav_auth.bento.R
import com.anubhav_auth.bento.ui.theme.MyFonts

@Composable
fun GrantLocationMode() {
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
            Text(text = "Grant Current Location", modifier = Modifier.width(240.dp), fontFamily = MyFonts.montserrat_semi_bold, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "This lets us show nearby restaurants you can order from", modifier = Modifier.width(240.dp), fontFamily = MyFonts.lato_regular, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(60.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                onClick = {},
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
                onClick = {},
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

@Preview(showBackground = true)
@Composable
private fun grantloc() {
    GrantLocationMode()
}