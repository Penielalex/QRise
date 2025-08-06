package com.example.qrise.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qrise.ui.component.AlarmComponent
import com.example.qrise.ui.component.ScrollableTimePicker

@Composable
fun CreateScreen(navback: () -> Unit) {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    var isAm by remember { mutableStateOf<Boolean?>(null) }

    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var alarmName by remember { mutableStateOf("") }
    var selectedSound by remember { mutableStateOf<String?>(null) }

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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            TextButton(
                onClick = {
                    navback()


                }
            ) {
                Text("Cancel", style=MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
            }

            TextButton(
                onClick = {
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
                }
            ) {
                Text("Save",style=MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
