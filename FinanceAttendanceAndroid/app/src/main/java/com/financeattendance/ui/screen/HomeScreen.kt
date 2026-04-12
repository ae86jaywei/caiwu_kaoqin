package com.financeattendance.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.financeattendance.ui.theme.FinanceAttendanceTheme

@Composable
fun HomeScreen() {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("财务", "考勤", "工资", "工作记录", "人员", "项目")
    val icons = listOf(
        Icons.Filled.AccountBalance,
        Icons.Filled.Schedule,
        Icons.Filled.Money,
        Icons.Filled.Note,
        Icons.Filled.Person,
        Icons.Filled.WorkspacePremium
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text("财务考勤系统")
        }
    }
}