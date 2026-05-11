# 启用数据录入功能 Spec

## Why
当前应用中六个功能模块（财务记录、考勤打卡、工资记录、工作记录、人员管理、项目管理）的按钮点击后无响应（`TODO` 占位符），用户无法添加任何数据。需要为所有功能模块添加完整的数据录入对话框功能。

## What Changes
- 为 **WorkRecordScreen** 添加添加工作记录的对话框
- 为 **PersonnelScreen** 添加添加人员的对话框
- 为 **ProjectScreen** 添加添加项目的对话框
- 验证 **FinanceScreen** 的编译错误已修复（createTime 参数缺失问题）
- 验证所有屏幕编译通过、构建成功

## Impact
- Affected specs: 无
- Affected code:
  - `app/src/main/java/com/financeattendance/ui/screen/WorkRecordScreen.kt`
  - `app/src/main/java/com/financeattendance/ui/screen/PersonnelScreen.kt`
  - `app/src/main/java/com/financeattendance/ui/screen/ProjectScreen.kt`
  - `app/src/main/java/com/financeattendance/ui/screen/FinanceScreen.kt`（验证）
  - `app/src/main/java/com/financeattendance/ui/screen/AttendanceScreen.kt`（验证）
  - `app/src/main/java/com/financeattendance/ui/screen/SalaryScreen.kt`（验证）

## ADDED Requirements
### Requirement: 工作记录录入功能
WorkRecordScreen 的"添加工作记录"按钮点击后弹出对话框，输入以下字段：
- 人员ID（必填）
- 工作日期（必填）
- 工作内容（必填）
- 项目ID（可选）

确认后调用 `WorkViewModel.addRecord()` 保存并刷新列表。

### Requirement: 人员录入功能
PersonnelScreen 的"添加人员"按钮点击后弹出对话框，输入以下字段：
- 姓名（必填）
- 人员类型（必填，如：固定员工、临时工）
- 参考日工资（可选）
- 联系电话（可选）
- 入职日期（可选）

确认后调用 `PersonnelViewModel.addPerson()` 保存并刷新列表。

### Requirement: 项目录入功能
ProjectScreen 的"添加项目"按钮点击后弹出对话框，输入以下字段：
- 项目名称（必填）
- 开始日期（必填）
- 结束日期（可选）
- 项目地址（可选）
- 客户名称（可选）
- 联系电话（可选）
- 合同金额（可选）
- 状态（必填，如：进行中、已完成、已暂停）
- 备注（可选）

确认后调用 `ProjectViewModel.addProject()` 保存并刷新列表。

### Requirement: 列表展示改进
所有屏幕从简单的列表展示改为使用 `LazyColumn` 分页显示（最多20条），支持显示更多记录详情。

### Requirement: 编译验证
确保所有屏幕文件编译通过，无参数缺失错误。