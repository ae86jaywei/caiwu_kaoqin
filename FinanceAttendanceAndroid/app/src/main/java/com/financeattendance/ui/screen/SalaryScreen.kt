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
import com.financeattendance.viewmodel.SalaryViewModel

@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val records by viewModel.records.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecords()
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
            Text("工资记录", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("记录数量: ${records.size}")
            
            Button(
                onClick = { /* TODO: 添加工资记录 */ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("添加工资记录")
            }
            
            // 简单显示记录列表
            if (records.isNotEmpty()) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    records.take(5).forEach { record ->
                        Text("- ${record.personId}: ${record.amount}元 (${record.payDate})")
                    }
                    if (records.size > 5) {
                        Text("... 还有${records.size - 5}条记录")
                    }
                }
            } else {
                Text("暂无工资记录", modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}