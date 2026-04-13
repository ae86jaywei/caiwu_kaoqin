package com.financeattendance.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.financeattendance.ui.theme.FinanceAttendanceTheme

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Finance,
        Screen.Attendance,
        Screen.Salary,
        Screen.WorkRecord,
        Screen.Personnel,
        Screen.Project
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Finance.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Finance.route) { FinanceScreen() }
            composable(Screen.Attendance.route) { AttendanceScreen() }
            composable(Screen.Salary.route) { SalaryScreen() }
            composable(Screen.WorkRecord.route) { WorkRecordScreen() }
            composable(Screen.Personnel.route) { PersonnelScreen() }
            composable(Screen.Project.route) { ProjectScreen() }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Finance : Screen(
        route = "finance",
        title = "财务",
        icon = Icons.Filled.AccountBalance
    )
    
    data object Attendance : Screen(
        route = "attendance",
        title = "考勤",
        icon = Icons.Filled.Schedule
    )
    
    data object Salary : Screen(
        route = "salary",
        title = "工资",
        icon = Icons.Filled.Money
    )
    
    data object WorkRecord : Screen(
        route = "work_record",
        title = "工作记录",
        icon = Icons.AutoMirrored.Filled.Note
    )
    
    data object Personnel : Screen(
        route = "personnel",
        title = "人员",
        icon = Icons.Filled.Person
    )
    
    data object Project : Screen(
        route = "project",
        title = "项目",
        icon = Icons.Filled.WorkspacePremium
    )
}