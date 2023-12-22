package com.parrosz.carecarb.ui.login

import android.content.Context
import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parrosz.carecarb.data.di.Injection
import com.parrosz.carecarb.data.local.datastore.LoginPreferences
import com.parrosz.carecarb.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)

    fun saveToken(token: String) {
        userRepository.saveToken(token)
    }
/*
    fun checkIfFirstTime(): LiveData<Boolean> {
        return userRepository.isFirstTime()
    }*/

    class LoginViewModelFactory private constructor(
        private val userRepository: UserRepository
    ) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userRepository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: LoginViewModelFactory? = null
            fun getInstance(
                context: Context
            ): LoginViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: LoginViewModelFactory(
                        Injection.provideUserRepository(context)
                    )
                }
        }
    }
}

