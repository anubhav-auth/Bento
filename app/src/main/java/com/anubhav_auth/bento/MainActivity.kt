package com.anubhav_auth.bento

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anubhav_auth.bento.authentication.AuthState
import com.anubhav_auth.bento.authentication.AuthViewModel
import com.anubhav_auth.bento.authentication.OTPVerificationPage
import com.anubhav_auth.bento.authentication.PhoneNumberEntryPage
import com.anubhav_auth.bento.userInterface.onboarding.OnboardingScreen
import com.anubhav_auth.bento.userInterface.testPage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        enableEdgeToEdge()
        setContent {

            val scope = rememberCoroutineScope()
            val authState by authViewModel.authState.collectAsState()

            Scaffold { paddingVal ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingVal)
                ) {

                    val startDestination = when (authState) {
                        is AuthState.Authenticated -> "homePage"
                        else -> "onboarding"
                    }


                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("error") {
                            ErrorScreen()
                        }
                        composable("onboarding") {
                            OnboardingScreen(scope) {
                                navController.navigate("loginPage")
                            }
                        }
                        composable("loginPage") {
                            PhoneNumberEntryPage(authViewModel = authViewModel, navController = navController)
                        }
                        composable("otpPage") {
                                OTPVerificationPage(
                                    navController = navController,
                                    authViewModel = authViewModel
                                )
                        }
                        composable("otpPage/{phoneNumber}") {
                            val phoneNumber = it.arguments?.getString("phoneNumber")
                            if (phoneNumber == null){
                                navController.navigate("error")
                            }else {
                                OTPVerificationPage(
                                    navController = navController,
                                    phoneNumber = phoneNumber,
                                    authViewModel = authViewModel
                                )
                            }
                        }
                        composable("homePage") {
                            testPage()
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    )
}