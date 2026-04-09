# 财务考勤综合系统开发任务清单

## 任务概览

本文档列出了开发财务考勤综合系统所需的所有开发任务，按照依赖关系和开发顺序组织。

## 开发任务

### 阶段一：项目初始化和基础架构

- [ ] 任务1：创建Android应用项目结构
  - 使用Android Studio创建新的Android项目
  - 配置项目基本信息（应用名称、包名等）
  - 设置最低SDK版本为26
  - 配置Kotlin和KSP插件
  - 添加Hilt依赖注入

- [ ] 任务2：创建项目目录结构
  - 创建ui目录（存放UI层）
  - 创建viewmodel目录（存放视图模型）
  - 创建data目录（存放数据层）
  - 创建domain目录（存放领域层）
  - 创建di目录（存放依赖注入）

- [ ] 任务3：配置依赖库
  - 添加Jetpack Compose依赖
  - 添加Room数据库依赖
  - 添加Hilt依赖
  - 添加Kotlin Coroutines依赖
  - 添加Apache POI（Excel导出）依赖

### 阶段二：数据模型层开发

- [ ] 任务4：创建财务记录数据模型
  - 文件：data/entity/FinanceRecord.kt
  - 定义FinanceRecord类，使用@Entity注解
  - 包含id、recordType、amount、date、projectId、projectName、remark、createTime字段

- [ ] 任务5：创建考勤记录数据模型
  - 文件：data/entity/AttendanceRecord.kt
  - 定义AttendanceRecord类
  - 定义CustomPeriod数据类

- [ ] 任务6：创建工资记录数据模型
  - 文件：data/entity/SalaryRecord.kt
  - 定义SalaryRecord类

- [ ] 任务7：创建人员信息数据模型
  - 文件：data/entity/Personnel.kt
  - 定义Personnel类

- [ ] 任务8：创建工作记录数据模型
  - 文件：data/entity/WorkRecord.kt
  - 定义WorkRecord类

- [ ] 任务9：创建工程项目数据模型
  - 文件：data/entity/Project.kt
  - 定义Project类
  - 定义ProjectPersonStats数据类
  - 定义ProjectExpenseStats数据类

### 阶段三：数据库层开发

- [ ] 任务10：创建Room数据库
  - 文件：data/database/AppDatabase.kt
  - 定义Database类，使用@Database注解
  - 配置版本号和导出Schema
  - 声明所有Entity

- [ ] 任务11：创建财务记录DAO
  - 文件：data/dao/FinanceDao.kt
  - 定义FinanceDao接口，使用@Dao注解
  - 实现增删改查方法

- [ ] 任务12：创建考勤记录DAO
  - 文件：data/dao/AttendanceDao.kt
  - 定义AttendanceDao接口
  - 支持按项目ID查询

- [ ] 任务13：创建工资记录DAO
  - 文件：data/dao/SalaryDao.kt
  - 定义SalaryDao接口

- [ ] 任务14：创建人员信息DAO
  - 文件：data/dao/PersonnelDao.kt
  - 定义PersonnelDao接口

- [ ] 任务15：创建工作记录DAO
  - 文件：data/dao/WorkDao.kt
  - 定义WorkDao接口

- [ ] 任务16：创建工程项目DAO
  - 文件：data/dao/ProjectDao.kt
  - 定义ProjectDao接口

### 阶段四：Repository层开发

- [ ] 任务17：创建财务Repository
  - 文件：data/repository/FinanceRepository.kt
  - 实现FinanceRepository接口
  - 封装FinanceDao调用

- [ ] 任务18：创建考勤Repository
  - 文件：data/repository/AttendanceRepository.kt
  - 实现AttendanceRepository接口
  - 封装AttendanceDao调用

- [ ] 任务19：创建工资Repository
  - 文件：data/repository/SalaryRepository.kt
  - 实现SalaryRepository接口

- [ ] 任务20：创建工作记录Repository
  - 文件：data/repository/WorkRepository.kt
  - 实现WorkRepository接口

- [ ] 任务21：创建人员Repository
  - 文件：data/repository/PersonnelRepository.kt
  - 实现PersonnelRepository接口

- [ ] 任务22：创建项目Repository
  - 文件：data/repository/ProjectRepository.kt
  - 实现ProjectRepository接口

### 阶段五：业务逻辑层开发

- [ ] 任务23：创建项目统计Service
  - 文件：domain/service/ProjectStatsService.kt
  - 实现项目人员工时统计
  - 实现项目费用自动汇总

### 阶段六：ViewModel层开发

- [ ] 任务24：创建财务ViewModel
  - 文件：viewmodel/FinanceViewModel.kt
  - 使用Hilt注入FinanceRepository
  - 管理财务记录状态

- [ ] 任务25：创建考勤ViewModel
  - 文件：viewmodel/AttendanceViewModel.kt
  - 使用Hilt注入AttendanceRepository

- [ ] 任务26：创建工资ViewModel
  - 文件：viewmodel/SalaryViewModel.kt
  - 使用Hilt注入SalaryRepository

- [ ] 任务27：创建工作记录ViewModel
  - 文件：viewmodel/WorkViewModel.kt
  - 使用Hilt注入WorkRepository

- [ ] 任务28：创建人员管理ViewModel
  - 文件：viewmodel/PersonnelViewModel.kt
  - 使用Hilt注入PersonnelRepository

- [ ] 任务29：创建项目管理ViewModel
  - 文件：viewmodel/ProjectViewModel.kt
  - 使用Hilt注入ProjectRepository
  - 使用Hilt注入ProjectStatsService

### 阶段七：UI层开发

- [ ] 任务30：创建主题配置
  - 文件：ui/theme/Color.kt
  - 文件：ui/theme/Theme.kt
  - 文件：ui/theme/Type.kt

- [ ] 任务31：创建首页
  - 文件：ui/screen/HomeScreen.kt
  - 实现底部导航栏
  - 实现页面切换

- [ ] 任务32：创建财务页面
  - 文件：ui/screen/FinanceScreen.kt
  - 实现财务记录列表展示
  - 实现添加财务记录弹窗
  - 实现查询和导出功能

- [ ] 任务33：创建考勤页面
  - 文件：ui/screen/AttendanceScreen.kt
  - 实现考勤记录列表展示
  - 实现打卡功能（上午、下午、加班、自定义）
  - 实现考勤统计展示

- [ ] 任务34：创建工资页面
  - 文件：ui/screen/SalaryScreen.kt
  - 实现工资记录列表展示
  - 实现添加工资发放记录

- [ ] 任务35：创建工作记录页面
  - 文件：ui/screen/WorkScreen.kt
  - 实现工作记录列表展示
  - 实现添加工作记录

- [ ] 任务36：创建人员管理页面
  - 文件：ui/screen/PersonnelScreen.kt
  - 实现人员列表展示
  - 实现添加/编辑/删除人员

- [ ] 任务37：创建项目管理页面
  - 文件：ui/screen/ProjectScreen.kt
  - 实现项目列表展示
  - 实现添加/编辑/删除项目
  - 实现项目详情展示

### 阶段八：依赖注入配置

- [ ] 任务38：配置Hilt模块
  - 文件：di/AppModule.kt
  - 配置Database实例
  - 配置所有Repository实例

### 阶段九：工具类开发

- [ ] 任务39：创建日期工具类
  - 文件：utils/DateUtils.kt
  - 实现日期格式化
  - 实现日期计算

- [ ] 任务40：创建导出工具类
  - 文件：utils/ExportUtils.kt
  - 实现数据导出为Excel

### 阶段十：测试和优化

- [ ] 任务41：功能测试
  - 测试财务记录功能
  - 测试考勤打卡功能
  - 测试工资计算功能
  - 测试工作记录功能
  - 测试人员管理功能
  - 测试项目管理功能
  - 测试项目统计功能

- [ ] 任务42：性能优化
  - 优化数据库查询
  - 优化列表渲染性能

- [ ] 任务43：UI优化
  - 优化页面布局
  - 优化交互体验
