package com.example.qrise.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.qrise.ui.component.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmApp(){


    val navController = rememberNavController()

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold (
        topBar = {
            if (currentRoute != "create screen") {
                CustomTopAppBar(
                    navToCreateScreen = { navController.navigate("create screen") }
                )
            }
        }
    ){
        Surface (
            modifier = Modifier.fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ){
            NavHost(navController=navController, startDestination = "front screen"){
                composable(route = "front screen"){
                    FrontScreen(navToEditScreen = {navController.navigate("")},
                        navToCreateScreen = {navController.navigate("create screen")})

                }
                composable(route = "create screen"){
                    CreateScreen(navback={navController.popBackStack()})

                }
            }

        }
    }

}