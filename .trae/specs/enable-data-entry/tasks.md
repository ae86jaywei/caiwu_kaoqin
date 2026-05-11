# Tasks
- [x] Task 1: WorkRecordScreen - 添加工作记录录入对话框
  - 添加 AlertDialog 弹出框，包含人员ID、工作日期、工作内容、项目ID字段
  - 确认后调用 WorkViewModel.addRecord() 保存并刷新列表
  - 将列表展示从简单 Column 改为 LazyColumn（最多20条）
- [x] Task 2: PersonnelScreen - 添加人员录入对话框
  - 添加 AlertDialog 弹出框，包含姓名、人员类型、参考日工资、联系电话、入职日期字段
  - 确认后调用 PersonnelViewModel.addPerson() 保存并刷新列表
  - 将列表展示从简单 Column 改为 LazyColumn（最多20条）
- [x] Task 3: ProjectScreen - 添加项目录入对话框
  - 添加 AlertDialog 弹出框，包含项目名称、开始日期、结束日期、地址、客户名称、联系电话、合同金额、状态、备注字段
  - 确认后调用 ProjectViewModel.addProject() 保存并刷新列表
  - 将列表展示从简单 Column 改为 LazyColumn（最多20条）
- [x] Task 4: 编译验证 - 确保所有 6 个屏幕文件编译通过
  - 确认 FinanceScreen 的 createTime 参数已修复
  - 确认 AttendanceScreen 和 SalaryScreen 的对话框功能完整
  - 确认所有文件无编译错误

# Task Dependencies
- 无依赖关系，Task 1-3 可并行执行
- Task 4 依赖 Task 1-3 完成