package com.financeattendance.di

import android.content.Context
import com.financeattendance.data.dao.AttendanceDao
import com.financeattendance.data.dao.FinanceDao
import com.financeattendance.data.dao.PersonnelDao
import com.financeattendance.data.dao.ProjectDao
import com.financeattendance.data.dao.SalaryDao
import com.financeattendance.data.dao.WorkDao
import com.financeattendance.data.database.AppDatabase
import com.financeattendance.data.repository.AttendanceRepository
import com.financeattendance.data.repository.FinanceRepository
import com.financeattendance.data.repository.PersonnelRepository
import com.financeattendance.data.repository.ProjectRepository
import com.financeattendance.data.repository.SalaryRepository
import com.financeattendance.data.repository.WorkRepository
import com.financeattendance.domain.service.ProjectStatsService
import com.financeattendance.viewmodel.AttendanceViewModel
import com.financeattendance.viewmodel.FinanceViewModel
import com.financeattendance.viewmodel.PersonnelViewModel
import com.financeattendance.viewmodel.ProjectViewModel
import com.financeattendance.viewmodel.SalaryViewModel
import com.financeattendance.viewmodel.WorkViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideFinanceDao(database: AppDatabase): FinanceDao = database.financeDao()

    @Provides
    fun provideAttendanceDao(database: AppDatabase): AttendanceDao = database.attendanceDao()

    @Provides
    fun provideSalaryDao(database: AppDatabase): SalaryDao = database.salaryDao()

    @Provides
    fun providePersonnelDao(database: AppDatabase): PersonnelDao = database.personnelDao()

    @Provides
    fun provideWorkDao(database: AppDatabase): WorkDao = database.workDao()

    @Provides
    fun provideProjectDao(database: AppDatabase): ProjectDao = database.projectDao()

    @Provides
    fun provideFinanceRepository(dao: FinanceDao): FinanceRepository = FinanceRepository(dao)

    @Provides
    fun provideAttendanceRepository(dao: AttendanceDao): AttendanceRepository = AttendanceRepository(dao)

    @Provides
    fun provideSalaryRepository(dao: SalaryDao): SalaryRepository = SalaryRepository(dao)

    @Provides
    fun providePersonnelRepository(dao: PersonnelDao): PersonnelRepository = PersonnelRepository(dao)

    @Provides
    fun provideWorkRepository(dao: WorkDao): WorkRepository = WorkRepository(dao)

    @Provides
    fun provideProjectRepository(dao: ProjectDao): ProjectRepository = ProjectRepository(dao)

    @Provides
    fun provideProjectStatsService(
        attendanceRepo: AttendanceRepository,
        financeRepo: FinanceRepository,
        personnelRepo: PersonnelRepository
    ): ProjectStatsService = ProjectStatsService(attendanceRepo, financeRepo, personnelRepo)
}
