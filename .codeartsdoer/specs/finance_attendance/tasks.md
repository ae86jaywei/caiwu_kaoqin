# 财务考勤综合系统开发任务清单

## 任务概览

本文档列出了开发财务考勤综合系统所需的所有开发任务，按照依赖关系和开发顺序组织。

## 开发任务

### 阶段一：项目初始化和基础架构

- [ ] 任务1：创建鸿蒙应用项目结构
  - 使用DevEco Studio创建新的鸿蒙应用项目
  - 配置项目基本信息（应用名称、包名等）
  - 设置最低API版本为4.0

- [ ] 任务2：创建项目目录结构
  - 创建model目录（存放数据模型）
  - 创建service目录（存放业务逻辑）
  - 创建dao目录（存放数据访问对象）
  - 创建database目录（存放数据库相关）
  - 创建utils目录（存放工具类）
  - 创建viewmodel目录（存放视图模型）
  - 创建pages目录（存放页面）

- [ ] 任务3：配置数据库权限
  - 在module.json5中配置数据库权限
  - 添加requestPermissions配置项

### 阶段二：数据模型层开发

- [ ] 任务4：创建财务记录数据模型
  - 文件：model/FinanceRecord.ets
  - 定义FinanceRecord类，包含id、recordType、amount、date、projectId、projectName、remark、createTime字段
  - 使用@Observed装饰器

- [ ] 任务5：创建考勤记录数据模型
  - 文件：model/AttendanceRecord.ets
  - 定义AttendanceRecord类，包含考勤相关字段（新增projectId字段）
  - 定义CustomPeriod接口

- [ ] 任务6：创建工资记录数据模型
  - 文件：model/SalaryRecord.ets
  - 定义SalaryRecord类

- [ ] 任务7：创建人员信息数据模型
  - 文件：model/Personnel.ets
  - 定义Personnel类

- [ ] 任务8：创建工作记录数据模型
  - 文件：model/WorkRecord.ets
  - 定义WorkRecord类

- [ ] 任务9：创建工程项目数据模型
  - 文件：model/Project.ets
  - 定义Project类，包含项目基本信息
  - 定义ProjectPersonStats接口（项目人员统计）
  - 定义ProjectExpenseStats接口（项目费用统计）

### 阶段三：数据库层开发

- [ ] 任务10：创建数据库辅助类
  - 文件：database/DatabaseHelper.ets
  - 实现数据库初始化
  - 创建数据表（finance_record、attendance_record、salary_record、personnel、work_record、project）
  - 提供数据库连接获取方法

- [ ] 任务11：创建财务记录DAO
  - 文件：dao/FinanceDao.ets
  - 实现增删改查方法

- [ ] 任务12：创建考勤记录DAO
  - 文件：dao/AttendanceDao.ets
  - 实现增删改查方法
  - 支持按项目ID查询

- [ ] 任务13：创建工资记录DAO
  - 文件：dao/SalaryDao.ets
  - 实现增删改查方法

- [ ] 任务14：创建人员信息DAO
  - 文件：dao/PersonnelDao.ets
  - 实现增删改查方法

- [ ] 任务15：创建工作记录DAO
  - 文件：dao/WorkDao.ets
  - 实现增删改查方法

- [ ] 任务16：创建工程项目DAO
  - 文件：dao/ProjectDao.ets
  - 实现增删改查方法

### 阶段四：业务逻辑层开发

- [ ] 任务17：创建财务服务
  - 文件：service/FinanceService.ets
  - 实现财务记录的增删改查
  - 实现数据导出功能

- [ ] 任务18：创建考勤服务
  - 文件：service/AttendanceService.ets
  - 实现打卡功能（上午、下午、加班、自定义）
  - 支持打卡时关联项目
  - 实现工作时长计算

- [ ] 任务19：创建工资服务
  - 文件：service/SalaryService.ets
  - 实现工资发放记录
  - 实现日工资计算
  - 实现工资标准设置

- [ ] 任务20：创建工作记录服务
  - 文件：service/WorkService.ets
  - 实现工作记录的增删改查

- [ ] 任务21：创建人员服务
  - 文件：service/PersonnelService.ets
  - 实现人员信息的增删改查

- [ ] 任务22：创建项目服务
  - 文件：service/ProjectService.ets
  - 实现项目信息的增删改查

- [ ] 任务23：创建项目统计服务
  - 文件：service/ProjectStatsService.ets
  - 实现项目人员工时统计
  - 实现项目费用自动汇总（材料费、运输费、办公用品、生活食材）

### 阶段五：工具类开发

- [ ] 任务24：创建日期工具类
  - 文件：utils/DateUtils.ets
  - 实现日期格式化
  - 实现日期计算

- [ ] 任务25：创建导出工具类
  - 文件：utils/ExportUtils.ets
  - 实现数据导出为Excel

### 阶段六：视图模型层开发

- [ ] 任务26：创建财务视图模型
  - 文件：viewmodel/FinanceViewModel.ets
  - 管理财务记录状态
  - 调用FinanceService

- [ ] 任务27：创建考勤视图模型
  - 文件：viewmodel/AttendanceViewModel.ets
  - 管理考勤记录状态
  - 调用AttendanceService

- [ ] 任务28：创建工资视图模型
  - 文件：viewmodel/SalaryViewModel.ets
  - 管理工资记录状态
  - 调用SalaryService

- [ ] 任务29：创建工作记录视图模型
  - 文件：viewmodel/WorkViewModel.ets
  - 管理工作记录状态
  - 调用WorkService

- [ ] 任务30：创建人员管理视图模型
  - 文件：viewmodel/PersonnelViewModel.ets
  - 管理人员信息状态
  - 调用PersonnelService

- [ ] 任务31：创建项目管理视图模型
  - 文件：viewmodel/ProjectViewModel.ets
  - 管理项目信息状态
  - 调用ProjectService
  - 调用ProjectStatsService

### 阶段七：UI层开发

- [ ] 任务32：创建首页
  - 文件：pages/Index.ets
  - 实现底部导航栏
  - 实现页面切换

- [ ] 任务33：创建财务页面
  - 文件：pages/FinancePage.ets
  - 实现财务记录列表展示
  - 实现添加财务记录弹窗（支持选择项目）
  - 实现查询和导出功能

- [ ] 任务34：创建考勤页面
  - 文件：pages/AttendancePage.ets
  - 实现考勤记录列表展示
  - 实现打卡功能（上午、下午、加班、自定义）
  - 支持打卡时选择项目
  - 实现考勤统计展示

- [ ] 任务35：创建工资页面
  - 文件：pages/SalaryPage.ets
  - 实现工资记录列表展示
  - 实现添加工资发放记录
  - 实现工资统计

- [ ] 任务36：创建工作记录页面
  - 文件：pages/WorkPage.ets
  - 实现工作记录列表展示
  - 实现添加工作记录

- [ ] 任务37：创建人员管理页面
  - 文件：pages/PersonnelPage.ets
  - 实现人员列表展示
  - 实现添加/编辑/删除人员

- [ ] 任务38：创建项目管理页面
  - 文件：pages/ProjectPage.ets
  - 实现项目列表展示
  - 实现添加/编辑/删除项目
  - 实现项目详情展示（人员工时统计、费用汇总）

### 阶段八：测试和优化

- [ ] 任务39：功能测试
  - 测试财务记录功能
  - 测试考勤打卡功能（含项目关联）
  - 测试工资计算功能
  - 测试工作记录功能
  - 测试人员管理功能
  - 测试项目管理功能
  - 测试项目统计功能（人员工时、费用汇总）

- [ ] 任务40：性能优化
  - 优化数据库查询
  - 优化列表渲染性能

- [ ] 任务41：UI优化
  - 优化页面布局
  - 优化交互体验
