package com.financeattendance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.financeattendance.viewmodel.PersonnelViewModel

@Composable
fun PersonnelScreen(
    viewModel: PersonnelViewModel = hiltViewModel()
) {
    val persons by viewModel.persons.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPersons()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("人员管理", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("人员数量: ${persons.size}")
            
            Button(
                onClick = { /* TODO: 添加人员 */ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("添加人员")
            }
            
            // 简单显示人员列表
            if (persons.isNotEmpty()) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    persons.take(5).forEach { person ->
                        Text("- ${person.name} (${person.position})")
                    }
                    if (persons.size > 5) {
                        Text("... 还有${persons.size - 5}位人员")
                    }
                }
            } else {
                Text("暂无人员数据", modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}