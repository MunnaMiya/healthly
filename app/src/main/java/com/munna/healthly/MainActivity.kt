package com.munna.healthly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.munna.healthly.domain.model.UserProfile
import com.munna.healthly.domain.usecase.GetUserProfileUseCase
import com.munna.healthly.presentation.navigation.AppNavHost
import com.munna.healthly.presentation.theme.HealthlyTheme
import kotlinx.coroutines.flow.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthlyTheme {
                val getProfile: GetUserProfileUseCase = viewModel()
                val profile by getProfile().collectAsStateWithLifecycle(null)
                AppNavHost(userProfile = profile)
            }
        }
    }
}
