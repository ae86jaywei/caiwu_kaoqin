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
import com.financeattendance.data.entity.SalaryRecord
import com.financeattendance.viewmodel.SalaryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel = hiltViewModel()
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
            Text("工资记录", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage != null && errorMessage!!.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("记录数量: ${records.size}", modifier = Modifier.padding(bottom = 16.dp))
            
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("添加工资记录")
            }
            
            if (records.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(records.take(20)) { record ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("- 人员ID:${record.personId} 应发:${record.shouldPay}元 实发:${record.actualPay}元 (${record.payDate})")
                            if (!record.remark.isNullOrEmpty()) {
                                Text("  备注: ${record.remark}", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
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
                Text("暂无工资记录")
            }
        }
    }

    if (showAddDialog) {
        AddSalaryRecordDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { personId, payDate, workDays, dailySalary, shouldPay, actualPay, remark ->
                val record = SalaryRecord(
                    personId = personId,
                    payDate = payDate,
                    workDays = workDays,
                    dailySalary = dailySalary,
                    shouldPay = shouldPay,
                    actualPay = actualPay,
                    remark = if (remark.isNotEmpty()) remark else null,
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                viewModel.addRecord(record)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSalaryRecordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, Double, Double, Double, String) -> Unit
) {
    var personId by remember { mutableStateOf("") }
    var payDate by remember { mutableStateOf("") }
    var workDays by remember { mutableStateOf("") }
    var dailySalary by remember { mutableStateOf("") }
    var shouldPay by remember { mutableStateOf("") }
    var actualPay by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加工资记录") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = personId,
                    onValueChange = { personId = it },
                    label = { Text("人员ID") },
                    placeholder = { Text("请输入人员ID") }
                )
                OutlinedTextField(
                    value = payDate,
                    onValueChange = { payDate = it },
                    label = { Text("发放日期") },
                    placeholder = { Text("如：2024-01-15") }
                )
                OutlinedTextField(
                    value = workDays,
                    onValueChange = { workDays = it },
                    label = { Text("工作天数") },
                    placeholder = { Text("请输入工作天数") }
                )
                OutlinedTextField(
                    value = dailySalary,
                    onValueChange = { dailySalary = it },
                    label = { Text("日工资标准") },
                    placeholder = { Text("请输入日工资标准") }
                )
                OutlinedTextField(
                    value = shouldPay,
                    onValueChange = { shouldPay = it },
                    label = { Text("应发金额") },
                    placeholder = { Text("请输入应发金额") }
                )
                OutlinedTextField(
                    value = actualPay,
                    onValueChange = { actualPay = it },
                    label = { Text("实发金额") },
                    placeholder = { Text("请输入实发金额") }
                )
                OutlinedTextField(
                    value = remark,
                    onValueChange = { remark = it },
                    label = { Text("备注（可选）") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val workDaysValue = workDays.toIntOrNull() ?: 0
                    val dailySalaryValue = dailySalary.toDoubleOrNull() ?: 0.0
                    val shouldPayValue = shouldPay.toDoubleOrNull() ?: 0.0
                    val actualPayValue = actualPay.toDoubleOrNull() ?: 0.0
                    if (personId.isNotEmpty() && payDate.isNotEmpty()) {
                        onConfirm(personId, payDate, workDaysValue, dailySalaryValue, shouldPayValue, actualPayValue, remark)
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