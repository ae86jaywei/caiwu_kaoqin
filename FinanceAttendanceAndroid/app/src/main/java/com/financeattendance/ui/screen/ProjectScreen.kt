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
import com.financeattendance.data.entity.Project
import com.financeattendance.viewmodel.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectScreen(
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val projects by viewModel.projects.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

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
        ) {
            Text("项目管理", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage != null && errorMessage!!.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("项目数量: ${projects.size}", modifier = Modifier.padding(bottom = 16.dp))
            
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("添加项目")
            }
            
            if (projects.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(projects.take(20)) { project ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("- ${project.name} (合同金额: ${project.contractAmount}元)")
                            if (!project.address.isNullOrEmpty()) {
                                Text("  地址: ${project.address}", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
                            }
                            Text("  状态: ${project.status}", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
                        }
                    }
                    if (projects.size > 20) {
                        item {
                            Text("... 还有${projects.size - 20}个项目", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            } else {
                Text("暂无项目数据")
            }
        }
    }

    if (showAddDialog) {
        AddProjectDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, startDate, endDate, address, customerName, phone, contractAmount, status, remark ->
                val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val now = timeFormat.format(Date())
                val record = Project(
                    name = name,
                    startDate = startDate,
                    endDate = if (endDate.isNotEmpty()) endDate else null,
                    address = if (address.isNotEmpty()) address else null,
                    customerName = if (customerName.isNotEmpty()) customerName else null,
                    phone = if (phone.isNotEmpty()) phone else null,
                    contractAmount = contractAmount,
                    status = status,
                    remark = if (remark.isNotEmpty()) remark else null,
                    createTime = now
                )
                viewModel.addProject(record)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String, Double, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var contractAmount by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加项目") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("项目名称") },
                    placeholder = { Text("请输入项目名称") }
                )
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("开始日期") },
                    placeholder = { Text("如：2024-01-15") }
                )
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("结束日期（可选）") },
                    placeholder = { Text("如：2024-12-31") }
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("项目地址（可选）") }
                )
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("客户名称（可选）") }
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("联系电话（可选）") }
                )
                OutlinedTextField(
                    value = contractAmount,
                    onValueChange = { contractAmount = it },
                    label = { Text("合同金额") },
                    placeholder = { Text("请输入合同金额") }
                )
                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("状态") },
                    placeholder = { Text("进行中、已完成、已暂停") }
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
                    val amountValue = contractAmount.toDoubleOrNull() ?: 0.0
                    if (name.isNotEmpty() && startDate.isNotEmpty() && status.isNotEmpty()) {
                        onConfirm(name, startDate, endDate, address, customerName, phone, amountValue, status, remark)
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