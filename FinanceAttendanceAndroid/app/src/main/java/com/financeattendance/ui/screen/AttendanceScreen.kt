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
import com.financeattendance.data.entity.AttendanceRecord
import com.financeattendance.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val records by viewModel.records.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showClockDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadRecords("", "", "", "")
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("考勤记录", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage != null && errorMessage!!.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("记录数量: ${records.size}", modifier = Modifier.padding(bottom = 16.dp))
            
            Button(
                onClick = { showClockDialog = true },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("打卡")
            }
            
            if (records.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(records.take(20)) { record ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("- ${record.clockDate}: 上午${record.morningStart ?: "未打卡"} - ${record.morningEnd ?: ""}")
                            if (!record.afternoonStart.isNullOrEmpty()) {
                                Text("  下午${record.afternoonStart} - ${record.afternoonEnd ?: ""}", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
                            }
                            if (!record.overtimeStart.isNullOrEmpty()) {
                                Text("  加班${record.overtimeStart} - ${record.overtimeEnd ?: ""}", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
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
                Text("暂无考勤记录")
            }
        }
    }

    if (showClockDialog) {
        ClockInDialog(
            onDismiss = { showClockDialog = false },
            onConfirm = { personId, projectId, morningStart, morningEnd, afternoonStart, afternoonEnd ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val today = dateFormat.format(Date())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val now = timeFormat.format(Date())
                
                val record = AttendanceRecord(
                    personId = personId,
                    projectId = if (projectId.isNotEmpty()) projectId else null,
                    clockDate = today,
                    morningStart = if (morningStart.isNotEmpty()) morningStart else now,
                    morningEnd = morningEnd.ifEmpty { null },
                    afternoonStart = if (afternoonStart.isNotEmpty()) afternoonStart else null,
                    afternoonEnd = if (afternoonEnd.isNotEmpty()) afternoonEnd else null,
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                viewModel.addRecord(record)
                showClockDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockInDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String) -> Unit
) {
    var personId by remember { mutableStateOf("") }
    var projectId by remember { mutableStateOf("") }
    var morningStart by remember { mutableStateOf("") }
    var morningEnd by remember { mutableStateOf("") }
    var afternoonStart by remember { mutableStateOf("") }
    var afternoonEnd by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("打卡") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = personId,
                    onValueChange = { personId = it },
                    label = { Text("人员ID") },
                    placeholder = { Text("请输入人员ID") }
                )
                OutlinedTextField(
                    value = projectId,
                    onValueChange = { projectId = it },
                    label = { Text("项目ID（可选）") }
                )
                OutlinedTextField(
                    value = morningStart,
                    onValueChange = { morningStart = it },
                    label = { Text("上午开始时间") },
                    placeholder = { Text("如：08:30") }
                )
                OutlinedTextField(
                    value = morningEnd,
                    onValueChange = { morningEnd = it },
                    label = { Text("上午结束时间（可选）") },
                    placeholder = { Text("如：12:00") }
                )
                OutlinedTextField(
                    value = afternoonStart,
                    onValueChange = { afternoonStart = it },
                    label = { Text("下午开始时间（可选）") },
                    placeholder = { Text("如：13:30") }
                )
                OutlinedTextField(
                    value = afternoonEnd,
                    onValueChange = { afternoonEnd = it },
                    label = { Text("下午结束时间（可选）") },
                    placeholder = { Text("如：18:00") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (personId.isNotEmpty()) {
                        onConfirm(personId, projectId, morningStart, morningEnd, afternoonStart, afternoonEnd)
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