package com.example.qrise.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.qrise.ui.component.ScrollableTimePicker

@Composable
fun CreateScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ScrollableTimePicker { hour, minute, isAm ->
            val amPm = if (isAm == null) "" else if (isAm) "AM" else "PM"
            println("Selected Time: $hour:$minute $amPm")
        }
    }
}
