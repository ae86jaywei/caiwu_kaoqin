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
import com.financeattendance.data.entity.Personnel
import com.financeattendance.viewmodel.PersonnelViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PersonnelScreen(
    viewModel: PersonnelViewModel = hiltViewModel()
) {
    val persons by viewModel.persons.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

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
        ) {
            Text("人员管理", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            
            if (errorMessage != null && errorMessage!!.isNotEmpty()) {
                Text("错误: $errorMessage", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            
            Text("人员数量: ${persons.size}", modifier = Modifier.padding(bottom = 16.dp))
            
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("添加人员")
            }
            
            if (persons.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(persons.take(20)) { person ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("- ${person.name} (${person.personType})")
                            if (!person.phone.isNullOrEmpty()) {
                                Text("  电话: ${person.phone}", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
                            }
                            if (person.referenceSalary > 0) {
                                Text("  参考日工资: ${person.referenceSalary}元", fontSize = androidx.compose.material3.MaterialTheme.typography.bodySmall.fontSize)
                            }
                        }
                    }
                    if (persons.size > 20) {
                        item {
                            Text("... 还有${persons.size - 20}位人员", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            } else {
                Text("暂无人员数据")
            }
        }
    }

    if (showAddDialog) {
        AddPersonnelDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, personType, referenceSalary, phone, joinDate ->
                val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val now = timeFormat.format(Date())
                val record = Personnel(
                    name = name,
                    personType = personType,
                    referenceSalary = referenceSalary,
                    phone = if (phone.isNotEmpty()) phone else null,
                    joinDate = if (joinDate.isNotEmpty()) joinDate else null,
                    createTime = now
                )
                viewModel.addPerson(record)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonnelDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var personType by remember { mutableStateOf("") }
    var referenceSalary by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var joinDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加人员") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("姓名") },
                    placeholder = { Text("请输入姓名") }
                )
                OutlinedTextField(
                    value = personType,
                    onValueChange = { personType = it },
                    label = { Text("人员类型") },
                    placeholder = { Text("固定员工、临时工") }
                )
                OutlinedTextField(
                    value = referenceSalary,
                    onValueChange = { referenceSalary = it },
                    label = { Text("参考日工资（可选）") },
                    placeholder = { Text("请输入参考日工资标准") }
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("联系电话（可选）") },
                    placeholder = { Text("请输入联系电话") }
                )
                OutlinedTextField(
                    value = joinDate,
                    onValueChange = { joinDate = it },
                    label = { Text("入职日期（可选）") },
                    placeholder = { Text("如：2024-01-15") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val salaryValue = referenceSalary.toDoubleOrNull() ?: 0.0
                    if (name.isNotEmpty() && personType.isNotEmpty()) {
                        onConfirm(name, personType, salaryValue, phone, joinDate)
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