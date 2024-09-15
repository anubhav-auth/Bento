package com.anubhav_auth.bento.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anubhav_auth.bento.R
import com.anubhav_auth.bento.ui.theme.MyFonts


@Composable
fun PhoneNumberEntryPage(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
//    val authState by authViewModel.authState.collectAsState()
//
//
//    LaunchedEffect(authState) {
//        when (authState) {
//            is AuthState.Authenticated -> navController.navigate("homePage")
//            is AuthState.Error -> Toast.makeText(
//                context,
//                (authState as AuthState.Error).message, Toast.LENGTH_SHORT
//            ).show()
//
//            else -> Unit
//        }
//    }

    var phonenumber by remember {
        mutableStateOf("")
    }
    val phoneNumberRegex = Regex("[^0-9]")

    val onContinue:() -> Unit = {
        if (phonenumber.length < 10){
            Toast.makeText(context, "Enter a valid Phone Number", Toast.LENGTH_SHORT).show()
        }else{
            navController.navigate("otpPage/${phonenumber}")
            authViewModel.verifyPhoneNumber("+91$phonenumber", context)
        }
    }

    Box(modifier = Modifier
        .padding(15.dp)
        .fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.drone_delivery),
                contentDescription = "",
                modifier = Modifier.size(300.dp)
            )
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Get started with App",
                    fontSize = 24.sp,
                    fontFamily = MyFonts.montserrat_semi_bold,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Login or signup to use app",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Default
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Enter phone number",
                    fontSize = 18.sp,
                    fontFamily = MyFonts.lato_regular,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.height(9.dp))
                OutlinedTextField(
                    value = phonenumber,
                    onValueChange = {
                        val stripped = phoneNumberRegex.replace(it, "")
                        phonenumber = if (stripped.length >= 10) {
                            stripped.substring(0..9)
                        } else {
                            stripped
                        }
                    },
                    placeholder = { Text(text = "00000 00000") },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.india_flag),
                            contentDescription = "",
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    prefix = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VerticalDivider(
                                modifier = Modifier.height(25.dp),
                                thickness = 2.dp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "+91")
                        }
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        focusedPrefixColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedPrefixColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400
                    ),
                    visualTransformation = SpaceAfterFiveDigitsTransformation()

                )
            }

        }
        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .imePadding()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                onClick = {
                    onContinue()
                },
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text(text = "Continue", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "By clicking, I accept the Terms & Conditions & Privacy Policy", fontSize = 12.sp)
        }


    }
}



class SpaceAfterFiveDigitsTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        // Limit the input to 10 digits
        val trimmed = if (text.text.length > 10) text.text.substring(0, 10) else text.text

        var formattedText = ""

        for (i in trimmed.indices) {
            // Insert a space after the first 5 digits
            if (i == 5) formattedText += " "
            formattedText += trimmed[i]
        }

        return TransformedText(AnnotatedString(formattedText), spaceOffsetMapping)
    }

    private val spaceOffsetMapping = object : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int =
            when {
                offset <= 5 -> offset
                offset in 6..10 -> offset + 1 // Add 1 for the space
                else -> 11 // After the 10th digit, the max length should be 11 (10 digits + 1 space)
            }

        override fun transformedToOriginal(offset: Int): Int =
            when {
                offset <= 5 -> offset
                offset in 6..11 -> offset - 1 // Subtract 1 for the space
                else -> 10 // Max original offset is 10 (10 digits)
            }
    }
}