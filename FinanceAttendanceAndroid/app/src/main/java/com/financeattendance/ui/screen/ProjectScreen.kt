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
import com.financeattendance.viewmodel.ProjectViewModel

@Composable
fun ProjectScreen(
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val projects by viewModel.projects.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProjects()
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
            Text("项目管理", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage != null && errorMessage!!.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("项目数量: ${projects.size}")
            
            Button(
                onClick = { /* TODO: 添加项目 */ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("添加项目")
            }
            
            // 简单显示项目列表
            if (projects.isNotEmpty()) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    projects.take(5).forEach { project ->
                        Text("- ${project.name} (预算: ${project.budget}元)")
                    }
                    if (projects.size > 5) {
                        Text("... 还有${projects.size - 5}个项目")
                    }
                }
            } else {
                Text("暂无项目数据", modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}