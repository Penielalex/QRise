package com.example.qrise.ui.component

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults


import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.qrise.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmComponent(
    onChoiceSelected: (selectedDays: List<String>, alarmName: String, selectedSound: String) -> Unit

) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }


    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                mediaPlayer?.pause() // or stop()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    fun playSound(resId: Int) {
        mediaPlayer?.stop()
        mediaPlayer?.release() // stop previous
        mediaPlayer = null
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }
    var selectedDays by remember { mutableStateOf(listOf<String>()) }
    var alarmName by remember { mutableStateOf(TextFieldValue("")) }
    var showSoundDialog by remember { mutableStateOf(false) }
    var selectedSound by remember { mutableStateOf("Beep") }

    val days = listOf("Su", "M", "T", "W", "Th", "F", "S")

    val sounds = listOf(
        "Beep" to R.raw.beep,
        "All in one" to R.raw.all,
        "Rock n Role" to R.raw.rock,
        "Trumpets" to R.raw.trumpet
    )

    LaunchedEffect(selectedDays, selectedSound, alarmName) {
        onChoiceSelected(selectedDays, alarmName.text,selectedSound)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(topStart = 43.dp, topEnd = 43.dp)
            )
            .padding(16.dp)
    ) {
        // Weekdays row
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top=16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            days.forEach { day ->
                val isSelected = selectedDays.contains(day)

                Text(
                    text = day,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .clickable {
                            selectedDays = if (isSelected) {
                                // Remove day -> create new Set
                                selectedDays - day
                            } else {
                                // Add day -> create new Set
                                selectedDays + day
                            }

                            println("Selected days: $selectedDays")
                        }
                        .padding(8.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alarm name TextField
        val lineColor = MaterialTheme.colorScheme.secondary
        TextField(
            value = alarmName,
            onValueChange = { alarmName = it },
            placeholder = { Text("Alarm Name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val y = size.height - strokeWidth / 2

                    drawLine(
                        color = lineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Sound selector
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {showSoundDialog = true }
                .padding(vertical = 12.dp)
                .drawBehind {
                    // Draw bottom underline
                    val strokeWidth = 2.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
        ) {
            Text(
                modifier = Modifier.padding(start= 12.dp),
                text = "Alarm Sound",

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.padding(start= 14.dp),
                text = selectedSound,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)

            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Sound selection dialog
        if (showSoundDialog) {
            Dialog(onDismissRequest = { mediaPlayer?.stop()  // Stop sound when dialog is dismissed
                mediaPlayer?.release()  // Release the MediaPlayer
                mediaPlayer = null
                showSoundDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var tempSelectedSound by remember { mutableStateOf(selectedSound) }

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sounds.forEach { (name, resId) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        tempSelectedSound = name
                                        playSound(resId)
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = tempSelectedSound == name,
                                    onCheckedChange = {
                                        tempSelectedSound = name
                                        playSound(resId)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = name, color = MaterialTheme.colorScheme.onBackground)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                selectedSound = tempSelectedSound
                                mediaPlayer?.stop()
                                mediaPlayer?.release()
                                mediaPlayer = null
                                showSoundDialog = false

                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }


    }
}
