package com.anubhav_auth.bento

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.anubhav_auth.bento.authentication.AuthViewModel
import com.anubhav_auth.bento.location.GrantLocationMode
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private val sharedStateViewModel: SharedStateViewModel by lazy {
        ViewModelProvider(this)[SharedStateViewModel::class.java]
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var allPermissionGranted by sharedStateViewModel.allPermissionGranted

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                allPermissionGranted = permissions.values.all { it }
            }



        setContent {

            val scope = rememberCoroutineScope()
            val authState by authViewModel.authState.collectAsState()

            GrantLocationMode(
                sharedStateViewModel = sharedStateViewModel,
                requestPermissionLauncher = requestPermissionLauncher
            )
//            Scaffold { paddingVal ->
//                Box(
//                    modifier = Modifier
//                        .background(MaterialTheme.colorScheme.background)
//                        .padding(paddingVal)
//                ) {
//
//                    val startDestination = when (authState) {
//                        is AuthState.Authenticated -> "homePage"
//                        else -> "onboarding"
//                    }
//
//
//                    val navController = rememberNavController()
//
//                    NavHost(navController = navController, startDestination = startDestination) {
//                        composable("error") {
//                            ErrorScreen()
//                        }
//                        composable("onboarding") {
//                            OnboardingScreen(scope) {
//                                navController.navigate("loginPage")
//                            }
//                        }
//                        composable("loginPage") {
//                            PhoneNumberEntryPage(authViewModel = authViewModel, navController = navController)
//                        }
//                        composable("otpPage") {
//                                OTPVerificationPage(
//                                    navController = navController,
//                                    authViewModel = authViewModel
//                                )
//                        }
//                        composable("otpPage/{phoneNumber}") {
//                            val phoneNumber = it.arguments?.getString("phoneNumber")
//                            if (phoneNumber == null){
//                                navController.navigate("error")
//                            }else {
//                                OTPVerificationPage(
//                                    navController = navController,
//                                    phoneNumber = phoneNumber,
//                                    authViewModel = authViewModel
//                                )
//                            }
//                        }
//                        composable("homePage") {
//                            testPage()
//                        }
//                    }
//                }
//            }

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