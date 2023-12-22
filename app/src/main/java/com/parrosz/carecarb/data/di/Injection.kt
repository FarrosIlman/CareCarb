package com.parrosz.carecarb.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.parrosz.carecarb.data.local.datastore.LoginPreferences
import com.parrosz.carecarb.data.local.room.CareCarbDatabase
import com.parrosz.carecarb.data.remote.retrofit.ApiConfig
import com.parrosz.carecarb.data.remote.retrofit.ApiService
import com.parrosz.carecarb.data.repository.UserRepository
import com.parrosz.storyu.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/*import com.parrosz.carecarb.data.remote.retrofit.ApiConfig
import com.parrosz.carecarb.data.repository.StoryRepository
import com.parrosz.carecarb.data.repository.UserRepository
import com.parrosz.carecarb.utils.AppExecutors*/

private const val LOGIN_PREFERENCES = "login"

object Injection {

    private fun provideDatabase(context: Context): CareCarbDatabase {
        return CareCarbDatabase.getInstance(context)
    }

    /*fun provideStoryRepository(context: Context): StoryRepository {
        return StoryRepository.getInstance(provideApiService(), provideDatabase(context))
    }*/

    fun provideUserRepository(context: Context): UserRepository {
        return UserRepository.getInstance(
            provideApiService(),
            provideLoginPreferences(context)
        )
    }

    private fun providesPreferencesDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            migrations = listOf(SharedPreferencesMigration(context, LOGIN_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(LOGIN_PREFERENCES) })
    }

    private fun provideLoginPreferences(context: Context): LoginPreferences = LoginPreferences.getInstance(
        providesPreferencesDataStore(context)
    )

    private fun provideApiService(): ApiService {
        return ApiConfig.getApiService()
    }

}