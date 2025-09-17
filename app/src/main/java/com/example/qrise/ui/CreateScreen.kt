package com.example.qrise.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.qrise.ui.component.AlarmComponent
import com.example.qrise.ui.component.ScrollableTimePicker
import com.example.qrise.viewmodel.CreateScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateScreen(navback: () -> Unit) {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    var isAm by remember { mutableStateOf<Boolean?>(null) }

    var selectedDays by remember { mutableStateOf(listOf<String>()) }
    var alarmName by remember { mutableStateOf("") }
    var selectedSound by remember { mutableStateOf<String>("Bell") }
    val createScreenViewModel = hiltViewModel<CreateScreenViewModel>()
    val message by createScreenViewModel.message.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(message) {
        message?.let {
            scope.launch {
                when (it) {
                    "yes" -> {

                        navback() // navigate back after showing snackbar
                    }
                    "no" -> {

                    }
                }
                createScreenViewModel.clearMessage()
            }
        }
    }






    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant, // sleek background
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant, // text color
                        shape = RoundedCornerShape(16.dp), // rounded corners
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Optional icon
                            if (data.visuals.message.contains("❌")) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            } else if (data.visuals.message.contains("✅")) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = data.visuals.message.replace("❌", "").replace("✅", "").trim(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            )
        }
    )
    { padding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Time Picker
            ScrollableTimePicker { hour, minute, am ->
                selectedHour = hour
                selectedMinute = minute
                isAm = am
                val amPm = if (am == null) "" else if (am) "AM" else "PM"
                println("Selected Time: $hour:$minute $amPm")
            }

            // Alarm Component (days + name + sound)
            AlarmComponent { days, name, sound ->
                selectedDays = days
                alarmName = name
                selectedSound = sound
                println("Selected Choice: $days  $sound $name")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                TextButton(
                    onClick = {
                        navback()


                    }
                ) {
                    Text(
                        "Cancel",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                TextButton(
                    onClick = {

                        if (alarmName.isBlank() || selectedDays.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("❌ Please fill all fields")
                            }
                            return@TextButton
                        }
                        // Convert to 24-hour format
                        val hour24 = when {
                            isAm == null -> selectedHour // fallback
                            isAm == true && selectedHour == 12 -> 0
                            isAm == false && selectedHour != 12 -> selectedHour + 12
                            else -> selectedHour
                        }

                        println(
                            """
                         SAVED ALARM:
                         Time: $hour24:$selectedMinute
                         Days: $selectedDays
                         Name: $alarmName
                         Sound: $selectedSound
                         """.trimIndent()

                        )
                        createScreenViewModel.saveAlarm(
                            hour = hour24,
                            minute = selectedMinute,
                            name = alarmName,
                            daysOfWeek = selectedDays,
                            soundName = selectedSound
                        )



                    }
                ) {
                    Text(
                        "Save",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
