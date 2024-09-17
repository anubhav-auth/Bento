package com.anubhav_auth.bento

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.anubhav_auth.bento.api.BentoApiRepoImpl
import com.anubhav_auth.bento.api.RetroFitInstance
import com.anubhav_auth.bento.authentication.AuthState
import com.anubhav_auth.bento.authentication.AuthViewModel
import com.anubhav_auth.bento.authentication.OTPVerificationPage
import com.anubhav_auth.bento.authentication.PhoneNumberEntryPage
import com.anubhav_auth.bento.database.BentoDatabase
import com.anubhav_auth.bento.database.LocalDatabaseViewModel
import com.anubhav_auth.bento.location.GrantLocationMode
import com.anubhav_auth.bento.location.LocationViewmodel
import com.anubhav_auth.bento.location.MarkerLocation
import com.anubhav_auth.bento.location.SheetSearch
import com.anubhav_auth.bento.userInterface.HomePage
import com.anubhav_auth.bento.userInterface.onboarding.OnboardingScreen
import com.anubhav_auth.bento.viewmodels.BentoViewModel
import com.anubhav_auth.bento.viewmodels.SharedStateViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    private val localDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            BentoDatabase::class.java,
            "bento_db"
        ).build()
    }
    private val localDatabaseViewModel by viewModels<LocalDatabaseViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LocalDatabaseViewModel(
                    localDatabase.savedAddressDao()
                ) as T
            }
        }
    })
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private val sharedStateViewModel: SharedStateViewModel by lazy {
        ViewModelProvider(this)[SharedStateViewModel::class.java]
    }
    private val locationViewmodel: LocationViewmodel by lazy {
        ViewModelProvider(this)[LocationViewmodel::class.java]
    }
    private val bentoViewModel by viewModels<BentoViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BentoViewModel(
                    BentoApiRepoImpl(RetroFitInstance.api)
                ) as T
            }
        }
    })

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val allPermissionGranted by sharedStateViewModel.allPermissionGranted

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                sharedStateViewModel.allPermissionGranted.value = permissions.values.all { it }
            }



        setContent {

            val scope = rememberCoroutineScope()
            val authState by authViewModel.authState.collectAsState()
            val savedAddress by localDatabaseViewModel.savedAddress.collectAsState()
            var isLoading by remember {
                mutableStateOf(true)
            }
            LaunchedEffect(savedAddress) {
                isLoading = false
            }
            Scaffold { paddingVal ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingVal)
                ) {
                    val startDestination = when {
                        isLoading -> "splashScreen"
                        authState is AuthState.Authenticated && savedAddress.isNotEmpty() -> "homePage"
                        authState is AuthState.Authenticated -> "locationAccessPage"
                        authState is AuthState.Error -> "error"
                        else -> "onboarding"
                    }


                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("splashScreen") {
                            splashScreen
                        }
                        composable("error") {
                            ErrorScreen(navController)
                        }
                        composable("onboarding") {
                            OnboardingScreen(scope, authViewModel, navController) {
                                navController.navigate("loginPage")
                            }
                        }
                        composable("loginPage") {
                            PhoneNumberEntryPage(
                                authViewModel = authViewModel,
                                navController = navController
                            )
                        }
                        composable("otpPage") {
                            OTPVerificationPage(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        composable("otpPage/{phoneNumber}") {
                            val phoneNumber = it.arguments?.getString("phoneNumber")
                            if (phoneNumber == null) {
                                navController.navigate("error")
                            } else {
                                OTPVerificationPage(
                                    navController = navController,
                                    phoneNumber = phoneNumber,
                                    authViewModel = authViewModel
                                )
                            }
                        }
                        composable("locationAccessPage") {
                            GrantLocationMode(
                                sharedStateViewModel = sharedStateViewModel,
                                bentoViewModel = bentoViewModel,
                                localDatabaseViewModel = localDatabaseViewModel,
                                requestPermissionLauncher = requestPermissionLauncher,
                                scope = scope,
                                navController = navController
                            )
                        }
                        composable("markerPage") {
                            MarkerLocation(
                                scope = scope,
                                locationViewmodel = locationViewmodel,
                                sharedStateViewModel = sharedStateViewModel,
                                localDatabaseViewModel = localDatabaseViewModel,
                                bentoViewModel = bentoViewModel,
                                fusedLocationProviderClient = fusedLocationProviderClient,
                                navController = navController
                            )
                        }
                        composable("homePage") {
                            HomePage(
                                sharedStateViewModel,
                                localDatabaseViewModel,
                                scope,
                                bentoViewModel,
                                navController
                            )
                        }
                        composable("sheetSearch") {
                            SheetSearch(
                                scope = scope,
                                scaffoldState = null,
                                sharedStateViewModel = sharedStateViewModel,
                                bentoViewModel = bentoViewModel,
                                localDatabaseViewModel = localDatabaseViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ErrorScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    )
}