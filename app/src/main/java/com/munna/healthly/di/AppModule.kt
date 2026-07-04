package com.munna.healthly.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.munna.healthly.data.db.HealthlyDatabase
import com.munna.healthly.data.repository.ExerciseRepositoryImpl
import com.munna.healthly.data.repository.UserProfileRepositoryImpl
import com.munna.healthly.domain.repository.ExerciseRepository
import com.munna.healthly.domain.repository.UserProfileRepository
import com.munna.healthly.domain.usecase.CalculateNutritionPlanUseCase
import com.munna.healthly.domain.usecase.GenerateWeeklyScheduleUseCase
import com.munna.healthly.domain.usecase.GetExercisesUseCase
import com.munna.healthly.domain.usecase.GetUserProfileUseCase
import com.munna.healthly.domain.usecase.SaveUserProfileUseCase
import org.koin.androidx.room.androidRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.viewModel

val appModule: Module = module {
    // DataStore
    single { preferencesDataStore<androidx.datastore.preferences.core.Preferences>("user_profile") }

    // Room DB
    database { androidRoomDatabase(HealthlyDatabase::class) }

    // Repositories
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single<UserProfileRepository> { UserProfileRepositoryImpl(get()) }

    // UseCases
    single { CalculateNutritionPlanUseCase() }
    single { GenerateWeeklyScheduleUseCase(get()) }
    single { GetExercisesUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    single { SaveUserProfileUseCase(get()) }

    // ViewModels
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { PlannerViewModel(get()) }
    viewModel { ExerciseLibraryViewModel(get()) }
    viewModel { ExerciseDetailViewModel(get()) }
}
