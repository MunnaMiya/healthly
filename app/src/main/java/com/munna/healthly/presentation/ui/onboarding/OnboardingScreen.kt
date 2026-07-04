package com.munna.healthly.presentation.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.munna.healthly.domain.model.ActivityLevel
import com.munna.healthly.domain.model.FitnessLevel
import com.munna.healthly.domain.model.Gender
import com.munna.healthly.domain.model.GoalType
import com.munna.healthly.presentation.theme.HealthlyTheme
import com.munna.healthly.presentation.viewmodel.OnboardingViewModel
import kotlin.math.roundToInt

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val vm: OnboardingViewModel = viewModel()
    val state by vm.state.collectAsStateWithLifecycle()

    var expandedGender by remember { mutableStateOf(false) }
    var expandedActivity by remember { mutableStateOf(false) }
    var expandedGoal by remember { mutableStateOf(false) }
    var expandedLevel by remember { mutableStateOf(false) }

    HealthlyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Healthly Setup",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Let's calculate your perfect plan",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InputRow("Age", state.age, { vm.updateAge(it) }, KeyboardType.Number)
                        InputRow("Height (cm)", state.height, { vm.updateHeight(it) }, KeyboardType.Number)
                        InputRow("Weight (kg)", state.weight, { vm.updateWeight(it) }, KeyboardType.Decimal)
                        InputRow("Target kg/month", state.targetKg, { vm.updateTargetKg(it) }, KeyboardType.Decimal)

                        // Gender Dropdown
                        DropdownRow("Gender", state.gender.name, expandedGender, { expandedGender = !expandedGender }) {
                            Gender.values().forEach { g ->
                                DropdownMenuItem(text = { Text(g.name) }, onClick = { vm.updateGender(g); expandedGender = false })
                            }
                        }

                        // Activity Dropdown
                        DropdownRow("Activity", state.activity.name.replace("_", " "), expandedActivity, { expandedActivity = !expandedActivity }) {
                            ActivityLevel.values().forEach { a ->
                                DropdownMenuItem(text = { Text(a.name.replace("_", " ")) }, onClick = { vm.updateActivity(a); expandedActivity = false })
                            }
                        }

                        // Goal Dropdown
                        DropdownRow("Goal", state.goal.name.replace("_", " "), expandedGoal, { expandedGoal = !expandedGoal }) {
                            GoalType.values().forEach { g ->
                                DropdownMenuItem(text = { Text(g.name.replace("_", " ")) }, onClick = { vm.updateGoal(g); expandedGoal = false })
                            }
                        }

                        // Fitness Level Dropdown
                        DropdownRow("Level", state.level.name, expandedLevel, { expandedLevel = !expandedLevel }) {
                            FitnessLevel.values().forEach { l ->
                                DropdownMenuItem(text = { Text(l.name) }, onClick = { vm.updateLevel(l); expandedLevel = false })
                            }
                        }

                        // Days Slider
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Days/Week: ${state.days}",
                                style = androidx.compose.material3.MaterialTheme.typography.labelLarge
                            )
                            Slider(
                                value = state.days.toFloat(),
                                onValueChange = { vm.updateDays(it.roundToInt()) },
                                valueRange = 2f..6f,
                                steps = 4
                            )
                        }

                        state.error?.let {
                            Text(
                                it,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                            )
                        }

                        Button(onClick = { vm.save(onFinish) }, modifier = Modifier.fillMaxWidth()) {
                            Text("Generate My Plan", style = androidx.compose.material3.MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputRow(label: String, value: String, onChange: (String) -> Unit, keyboard: KeyboardType = KeyboardType.Text) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = androidx.compose.material3.MaterialTheme.typography.labelLarge)
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboard),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DropdownRow(label: String, current: String, expanded: Boolean, onToggle: () -> Unit, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = androidx.compose.material3.MaterialTheme.typography.labelLarge)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onToggle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = current,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                singleLine = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onToggle
            ) { content() }
        }
    }
}
