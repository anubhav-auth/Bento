package com.anubhav_auth.bento

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anubhav_auth.bento.ui.theme.BentoTheme
import com.anubhav_auth.bento.userInterface.authentication.AuthState
import com.anubhav_auth.bento.userInterface.authentication.AuthViewModel
import com.anubhav_auth.bento.userInterface.authentication.authtestui
import com.anubhav_auth.bento.userInterface.onboarding.OnboardingScreen
import com.anubhav_auth.bento.userInterface.testPage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val scope = rememberCoroutineScope()

            Scaffold { paddingVal ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingVal)
                ) {
                    val collectAsState by authViewModel.authState.collectAsState()
                    var startDestination = "error"

                    if (collectAsState is AuthState.Authenticated) {
                        startDestination = "testPage"
                    }else if (collectAsState is AuthState.Unauthenticated) {
                        startDestination = "onboarding"
                    }


                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("error") {
                            ErrorScreen()
                        }
                        composable("onboarding") {
                            OnboardingScreen(scope){
                                navController.navigate("loginPage")
                            }
                        }
                        composable("loginPage") {
                            authtestui(authViewModel = authViewModel, navController = navController)
                        }
                        composable("testPage") {
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
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red))
}