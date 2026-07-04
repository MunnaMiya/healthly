package com.munna.healthly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.munna.healthly.presentation.ui.about.AboutScreen
import com.munna.healthly.presentation.ui.dashboard.DashboardScreen
import com.munna.healthly.presentation.ui.exercise.ExerciseDetailScreen
import com.munna.healthly.presentation.ui.exercise.ExerciseLibraryScreen
import com.munna.healthly.presentation.ui.onboarding.OnboardingScreen
import com.munna.healthly.presentation.ui.planner.PlannerScreen
import com.munna.healthly.domain.model.UserProfile

@Composable
fun AppNavHost(userProfile: UserProfile?) {
    val navController = rememberNavController()
    val startRoute = if (userProfile != null) "dashboard" else "onboarding"

    NavHost(navController, startRoute) {
        composable("onboarding") {
            OnboardingScreen(onFinish = { navController.navigate("dashboard") { popUpTo("onboarding") { inclusive = true } } })
        }
        composable("dashboard") {
            DashboardScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable("planner") {
            PlannerScreen()
        }
        composable("exercise_library") {
            ExerciseLibraryScreen(onExerciseClick = { id -> navController.navigate("exercise_detail/$id") })
        }
        composable(
            route = "exercise_detail/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: return@composable
            ExerciseDetailScreen(exerciseId = exerciseId)
        }
        composable("about") { AboutScreen() }
    }
}
