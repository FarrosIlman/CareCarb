package com.parrosz.carecarb.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.parrosz.carecarb.data.di.Injection
import com.parrosz.carecarb.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) :
    ViewModel() {
    fun registerUser(firstName: String, lastName: String, email: String, password: String) =
        userRepository.register(firstName, lastName, email, password)

    class RegisterViewModelFactory private constructor(private val userRepository: UserRepository) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(userRepository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: RegisterViewModelFactory? = null

            fun getInstance(context: Context): RegisterViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: RegisterViewModelFactory(Injection.provideUserRepository(context))
                }
        }
    }
}