package com.example.qrise.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FrontScreen(
    navToEditScreen: () -> Unit,
    navToCreateScreen: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp)
    ) {
        Text(
            text = "No Alarms are Set.",
            color = MaterialTheme.colorScheme.primary
        )

        // Clickable '+' icon
        IconButton(onClick = { navToCreateScreen() }) {
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = "Add Alarm",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
