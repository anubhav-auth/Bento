package com.anubhav_auth.bento.userInterface.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun authtestui(authViewModel: AuthViewModel, navController: NavController) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    var number by remember {
        mutableStateOf("")
    }

    var otp by remember {
        mutableStateOf("")
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> navController.navigate("testPage")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            TextField(value = number, onValueChange = {
                number = it
            })
            Button(onClick = { authViewModel.verifyPhoneNumber("+91$number", context) }) {
                Text(text = "Send OTP")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            TextField(value = otp, onValueChange = {
                otp = it
            })
            Button(onClick = { authViewModel.verifyCode(otp) }) {
                Text(text = "verify OTP")
            }
        }
    }
}