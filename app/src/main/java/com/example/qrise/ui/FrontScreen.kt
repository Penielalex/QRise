package com.example.qrise.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.qrise.viewmodel.FrontScreenViewModel

@Composable
fun FrontScreen(
    navToEditScreen: (alarmId: Int) -> Unit,
    navToCreateScreen: () -> Unit
) {
    val frontScreenViewModel = hiltViewModel<FrontScreenViewModel>()
    val alarms by frontScreenViewModel.alarms.collectAsState()
    val isLoading by frontScreenViewModel.isLoading.collectAsState()
    val message by frontScreenViewModel.message.collectAsState()

    // Fetch alarms when screen loads
    LaunchedEffect(Unit) {
        frontScreenViewModel.getAllAlarms()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp)
    ) {
        if (!isLoading) {
            androidx.compose.material3.CircularProgressIndicator()
        } else if (alarms.isEmpty()) {
            Text(
                text = "No Alarms are Set.",
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            // List of alarms
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                alarms.forEach { alarm ->
                    Text(
                        text = "${alarm.name} - ${alarm.hour}:${alarm.minute} - ${alarm.daysOfWeek}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { navToEditScreen(alarm.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // '+' Icon to create new alarm
        IconButton(onClick = { navToCreateScreen() }) {
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = "Add Alarm",
                tint = MaterialTheme.colorScheme.primary,
                //modifier = Modifier.size(48.dp)
            )
        }

        // Optional: show messages (like DB errors)
        message?.let {
            androidx.compose.material3.Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
