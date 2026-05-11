package com.financeattendance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.financeattendance.data.entity.WorkRecord
import com.financeattendance.viewmodel.WorkViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WorkRecordScreen(
    viewModel: WorkViewModel = hiltViewModel()
) {
    val records by viewModel.records.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadRecords(null)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("工作记录", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage != null && errorMessage!!.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("记录数量: ${records.size}", modifier = Modifier.padding(bottom = 16.dp))
            
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("添加工作记录")
            }
            
            if (records.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(records.take(20)) { record ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("- ${record.workDate}: ${record.workContent}")
                            if (!record.projectId.isNullOrEmpty()) {
                                Text("  项目ID: ${record.projectId}", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
                            }
                        }
                    }
                    if (records.size > 20) {
                        item {
                            Text("... 还有${records.size - 20}条记录", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            } else {
                Text("暂无工作记录")
            }
        }
    }

    if (showAddDialog) {
        AddWorkRecordDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { personId, workDate, workContent, projectId ->
                val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val now = timeFormat.format(Date())
                val record = WorkRecord(
                    personId = personId,
                    workDate = workDate,
                    workContent = workContent,
                    projectId = if (projectId.isNotEmpty()) projectId else null,
                    createTime = now
                )
                viewModel.addRecord(record)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkRecordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var personId by remember { mutableStateOf("") }
    var workDate by remember { mutableStateOf("") }
    var workContent by remember { mutableStateOf("") }
    var projectId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加工作记录") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = personId,
                    onValueChange = { personId = it },
                    label = { Text("人员ID") },
                    placeholder = { Text("请输入人员ID") }
                )
                OutlinedTextField(
                    value = workDate,
                    onValueChange = { workDate = it },
                    label = { Text("工作日期") },
                    placeholder = { Text("如：2024-01-15") }
                )
                OutlinedTextField(
                    value = workContent,
                    onValueChange = { workContent = it },
                    label = { Text("工作内容") },
                    placeholder = { Text("请输入工作内容") }
                )
                OutlinedTextField(
                    value = projectId,
                    onValueChange = { projectId = it },
                    label = { Text("项目ID（可选）") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (personId.isNotEmpty() && workDate.isNotEmpty() && workContent.isNotEmpty()) {
                        onConfirm(personId, workDate, workContent, projectId)
                    }
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}