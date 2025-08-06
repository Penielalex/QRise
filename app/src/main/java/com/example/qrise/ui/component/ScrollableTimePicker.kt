package com.example.qrise.ui.component

import android.text.format.DateFormat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollableTimePicker(
    onTimeSelected: (hour: Int, minute: Int, isAm: Boolean?) -> Unit
) {
    val context = LocalContext.current
    val is24Hour = DateFormat.is24HourFormat(context)

    val hours = if (is24Hour) (0..23).toList() else (1..12).toList()
    val minutes = (0..59).toList()
    val amPmOptions = listOf("AM", "PM")

    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val amPmState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    var selectedHour by remember { mutableStateOf(hours.first()) }
    var selectedMinute by remember { mutableStateOf(0) }
    var isAm by remember { mutableStateOf(true) } // Only for 12-hour mode











    LaunchedEffect(selectedHour, selectedMinute, isAm) {
        onTimeSelected(selectedHour, selectedMinute, if (is24Hour) null else isAm)
    }

    Box(
        modifier = Modifier
            .padding(30.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, Color.White)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
            .fillMaxWidth()
            .height(230.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hours Column with snapping
            InfiniteCircularList(
                numberOfDisplayedItems = 3,
                width = 100.dp,
                itemHeight = 70.dp,
                items = if (is24Hour) (0..23).map { "%02d".format(it) } else (1..12).map { "%02d".format(it) },
                initialItem = "07", // must now be a string since items are strings
                textStyle = MaterialTheme.typography.titleLarge,
                textColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                onItemSelected = { i, item ->
                    selectedHour = item.toInt() // convert back to Int if needed
                }
            )


            Text(":", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)

            // Minutes Column with snapping
            InfiniteCircularList(
                numberOfDisplayedItems = 3,
                width = 100.dp,
                itemHeight = 70.dp,
                items =  (0..59).map { "%02d".format(it) },
                initialItem = "00", // must now be a string since items are strings
                textStyle = MaterialTheme.typography.titleLarge,
                textColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                onItemSelected = { i, item ->
                    selectedMinute = item.toInt() // convert back to Int if needed
                }
            )

            // AM/PM Column only for 12-hour format
            if (!is24Hour) {
                InfiniteCircularList(
                    numberOfDisplayedItems = 2,
                    width = 100.dp,
                    itemHeight = 70.dp,
                    items = listOf("AM", "PM"),
                    initialItem = "AM", // must now be a string since items are strings
                    textStyle = MaterialTheme.typography.titleLarge,
                    textColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    onItemSelected = { i, item ->
                        if(item =="AM"){
                            isAm=true
                        }else{
                            isAm = false
                        }
                        // convert back to Int if needed
                    }
                )
            }
        }
    }

    // Update selected values when scrolling
    LaunchedEffect(hourState.firstVisibleItemIndex) {
        selectedHour = hours[hourState.firstVisibleItemIndex]
    }
    LaunchedEffect(minuteState.firstVisibleItemIndex) {
        selectedMinute = minutes[minuteState.firstVisibleItemIndex]
    }
    if (!is24Hour) {
        LaunchedEffect(amPmState.firstVisibleItemIndex) {
            isAm = amPmState.firstVisibleItemIndex == 0
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> InfiniteCircularList(
    width: Dp,
    itemHeight: Dp,
    numberOfDisplayedItems: Int,
    items: List<T>,
    initialItem: T,
    itemScaleFact: Float = 1.5f,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    onItemSelected: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    val itemHalfHeight = LocalDensity.current.run { itemHeight.toPx() / 2f }
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember {
        mutableStateOf(0)
    }
    var itemsState by remember {
        mutableStateOf(items)
    }
    LaunchedEffect(items) {
        var targetIndex = items.indexOf(initialItem) - 1
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        itemsState = items
        lastSelectedIndex = targetIndex
        scrollState.scrollToItem(targetIndex)
    }
    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(itemHeight * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(
            count = Int.MAX_VALUE,
            itemContent = { i ->
                val item = itemsState[i % itemsState.size]
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val y = coordinates.positionInParent().y - itemHalfHeight
                            val parentHalfHeight = (coordinates.parentCoordinates?.size?.height ?: 0) / 2f
                            val isSelected = (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)
                            if (isSelected && lastSelectedIndex != i) {
                                onItemSelected(i % itemsState.size, item)
                                lastSelectedIndex = i
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.toString(),
                        style = textStyle,
                        color = if (lastSelectedIndex == i) {
                            selectedTextColor
                        } else {
                            textColor
                        },
                        fontSize = if (lastSelectedIndex == i) {
                            textStyle.fontSize * itemScaleFact
                        } else {
                            textStyle.fontSize
                        }
                    )
                }
            }
        )
    }
}