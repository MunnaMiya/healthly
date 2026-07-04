package com.munna.healthly.presentation.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.munna.healthly.domain.model.DailySchedule
import com.munna.healthly.domain.model.NutritionPlan
import com.munna.healthly.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(onNavigate: (String) -> Unit) {
    val vm: DashboardViewModel = viewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Healthly") },
                navigationIcon = {
                    IconButton(onClick = { onNavigate("profile") }) { Icon(Icons.Default.Menu, "") }
                },
                actions = {
                    IconButton(onClick = { onNavigate("about") }) { Icon(Icons.Default.Info, "") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigate("planner") },
                icon = { Icon(Icons.Default.CalendarMonth, "") },
                text = { Text("Weekly Plan") }
            )
        }
    ) { padding ->
        state.nutritionPlan?.let { plan ->
            state.profile?.let {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { BmiCalorieCard(plan = plan) }
                    item { MacroBreakdownRow(plan = plan) }
                    item { SectionHeader("Today's Routine", onClickAll = { onNavigate("exercise_library") }) }
                    item { TodayWorkoutCard(schedule = state.todaySchedule, onExerciseClick = { id -> onNavigate("exercise_detail/$id") }) }
                    item { MicrosCard(plan = plan) }
                    item { WaterCard(plan = plan) }
                }
            }
        }
    }
}

@Composable
fun BmiCalorieCard(plan: NutritionPlan) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("BMI", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(String.format("%.1f", plan.bmi), style = MaterialTheme.typography.headlineLarge)
                Text(bmiCategory(plan.bmi), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Daily Budget", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${plan.targetCalories} kcal", style = MaterialTheme.typography.headlineLarge)
                Text("${plan.goal.name.replace("_", " ")} Mode", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun MacroBreakdownRow(plan: NutritionPlan) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MacroCard("Protein", "${plan.proteinGrams}g", plan.proteinGrams * 4, plan.targetCalories, Icons.Default.FitnessCenter, MaterialTheme.colorScheme.tertiaryContainer, Modifier.weight(1f))
        MacroCard("Carbs", "${plan.carbsGrams}g", plan.carbsGrams * 4, plan.targetCalories, Icons.Default.LocalFireDepartment, MaterialTheme.colorScheme.secondaryContainer, Modifier.weight(1f))
        MacroCard("Fat", "${plan.fatGrams}g", plan.fatGrams * 9, plan.targetCalories, Icons.Default.LocalFireDepartment, MaterialTheme.colorScheme.errorContainer, Modifier.weight(1f))
    }
}

@Composable
fun MacroCard(title: String, amount: String, cals: Int, total: Int, icon: ImageVector, bgColor: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    val pct = if (total > 0) (cals * 100 / total) else 0
    Card(modifier = modifier.height(100.dp), colors = CardDefaults.cardColors(containerColor = bgColor)) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Icon(icon, "", tint = MaterialTheme.colorScheme.onSurface)
                Text("$pct%", style = MaterialTheme.typography.labelLarge)
            }
            Text(amount, style = MaterialTheme.typography.headlineMedium)
            Text(title, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun SectionHeader(title: String, onClickAll: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        TextButton(onClick = onClickAll) { Text("See All") }
    }
}

@Composable
fun TodayWorkoutCard(schedule: DailySchedule?, onExerciseClick: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            schedule?.exercises?.forEach { ex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "${ex.reps} × ${ex.sets} sets",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text("Exercise: ${ex.exerciseId}", style = MaterialTheme.typography.bodyLarge)
                    }
                    IconButton(onClick = { onExerciseClick(ex.exerciseId) }) {
                        Icon(Icons.Default.FitnessCenter, "")
                    }
                }
                if (ex != schedule.exercises.last()) Divider()
            } ?: Text(
                "No workout today. Rest!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MicrosCard(plan: NutritionPlan) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Micronutrients & Fiber", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MicroItem("Fiber", "${plan.fiberGrams}g")
                MicroItem("Sodium", "${plan.sodiumMg}mg")
                MicroItem("Potassium", "${plan.potassiumMg}mg")
                MicroItem("Iron", "${plan.ironMg}mg")
            }
        }
    }
}

@Composable
fun MicroItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun WaterCard(plan: NutritionPlan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WaterDrop, "", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Daily Water Target", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "${plan.waterMl / 1000}.${(plan.waterMl % 1000) / 100}L",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            Text("Track intake", style = MaterialTheme.typography.labelLarge)
        }
    }
}

fun bmiCategory(bmi: Float): String = when {
    bmi < 18.5 -> "Underweight"
    bmi < 25 -> "Normal"
    bmi < 30 -> "Overweight"
    else -> "Obese"
}
