package com.anubhav_auth.bento.authentication

import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anubhav_auth.bento.ui.theme.MyFonts


@Composable
fun OTPVerificationPage(
    otpLength: Int = 6,
    navController: NavController,
    phoneNumber: String = "0000000000",
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(otpLength) { FocusRequester() }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> navController.navigate("locationAccessPage")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    val onContinue: () -> Unit = {
        if (otpValues.joinToString("").length < 6 || otpValues.joinToString("").length > 6) {
            Toast.makeText(context, "Enter the OTP correctly", Toast.LENGTH_SHORT).show()
        } else {
            authViewModel.verifyCode(otpValues.joinToString(""))
        }
    }

    Box(modifier = Modifier.padding(12.dp)) {
        Column(
            modifier = Modifier

                .fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .size(27.dp)
                    .clickable { navController.navigateUp() }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Verify your details",
                fontSize = 30.sp,
                fontFamily = MyFonts.montserrat_semi_bold,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Enter OTP sent to +91 $phoneNumber via sms",
                fontSize = 15.sp,
                fontFamily = FontFamily.Default
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Enter OTP",
                fontSize = 18.sp,
                fontFamily = MyFonts.lato_regular,
                fontWeight = FontWeight.W600
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                otpValues.forEachIndexed { index, otpValue ->
                    OutlinedTextField(
                        value = otpValue,
                        onValueChange = {
                            if (it.length <= 1) {
                                otpValues[index] = it
                                if (it.isNotEmpty() && index < otpLength - 1) {
                                    focusRequesters[index + 1].requestFocus()
                                } else if (it.isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .focusRequester(focusRequesters[index])
                            .padding(4.dp),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(8.dp)
                    )
                    LaunchedEffect(Unit) {
                        if (index == 0) focusRequesters[index].requestFocus()
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Text(
                    text = "Didn't receive OTP?",
                    fontSize = 12.sp,
                    fontFamily = MyFonts.lato_regular,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.width(9.dp))
                Text(
                    text = "Resend",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable { },
                    color = Color(0xFF49CA3E)
                )
                Spacer(modifier = Modifier.width(50.dp))
                Timer()
            }


        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
        ) {
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
        }
    }

}

@Composable
fun Timer() {
    // Define a state to hold the remaining time
    val timerState = remember { mutableStateOf("60") }

    // Create a CountDownTimer
    LaunchedEffect(Unit) {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                timerState.value = String.format("%02d", seconds)
            }

            override fun onFinish() {
                timerState.value = "00"
            }
        }.start()
    }

    // Display the timer
    Text(
        text = "00:${timerState.value}",
        fontSize = 12.sp,
    )
}