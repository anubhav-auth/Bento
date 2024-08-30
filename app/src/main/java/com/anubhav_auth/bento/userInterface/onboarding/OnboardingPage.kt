package com.anubhav_auth.bento.userInterface.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anubhav_auth.bento.R
import com.anubhav_auth.bento.ui.theme.MyFonts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(scope: CoroutineScope, onfinish: () -> Unit) {
    val pagerState = rememberPagerState(
        pageCount = { 4 },
    )

    val pages = listOf(
        OnboardingPageData(
            title = "Welcome to Bento",
            description = "Discover the best food around you with Bento! Explore top local spots and order your favorites with ease.",
            imageResId = R.drawable.logo_no_bg
        ),
        OnboardingPageData(
            title = "Order with Ease",
            description = "Choose from a variety of dishes and get them delivered fast.",
            imageResId = R.drawable.female_delivering
        ),
        OnboardingPageData(
            title = "Dine-In Options",
            description = "Find top restaurants near you.",
            imageResId = R.drawable.dining_restaurant
        ),
        OnboardingPageData(
            title = "Track Your Order",
            description = "Real-time tracking of your order's delivery status.",
            imageResId = R.drawable.robot_delivery
        )

    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier, userScrollEnabled = false, ) { page: Int ->
            OnboardingPage(pages[page], scope, pagerState){
                onfinish()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingPage(
    onboardingPageData: OnboardingPageData,
    scope: CoroutineScope,
    pagerState: PagerState,
    onFinish: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = onboardingPageData.imageResId),
                contentDescription = null,
                modifier = Modifier.size(500.dp)
            )
            Text(
                text = onboardingPageData.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MyFonts.openSansBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = onboardingPageData.description,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                fontFamily = MyFonts.lato_regular,
                textAlign = TextAlign.Center
            )
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            if (pagerState.currentPage < 3) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Text(text = "Next", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    onClick = {
                        scope.launch {
                         pagerState.scrollToPage(4)
                        }
                    },
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = "Skip", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(21.dp))

            }else{

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    onClick = {
                        scope.launch {
                            onFinish()
                        }
                    },
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {

                    Text(text = "Get Started !!", fontSize = 18.sp)

                }
            }


        }

    }
}

data class OnboardingPageData(
    val title: String,
    val description: String,
    val imageResId: Int
)