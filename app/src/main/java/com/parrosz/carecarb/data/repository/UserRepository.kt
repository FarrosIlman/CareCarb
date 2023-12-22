package com.parrosz.carecarb.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.parrosz.carecarb.data.local.datastore.LoginPreferences
import com.parrosz.carecarb.data.Result
import com.parrosz.carecarb.data.remote.response.LoginResponse
import com.parrosz.carecarb.data.remote.retrofit.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserRepository (
    private val apiService: ApiService,
    private val loginPreferences: LoginPreferences
) : CoroutineScope {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(
                email,
                password
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(firstName: String, lastName: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(
                firstName,
                lastName,
                email,
                password
            )
            if (response.error) {
                emit(Result.Error(response.message))
            } else {
                emit(Result.Success(response))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getToken(): LiveData<String> = loginPreferences.getToken().asLiveData()

    fun deleteToken() {
        launch(Dispatchers.IO) {
            loginPreferences.deleteToken()
        }
    }

    fun saveToken(token: String) {
        launch(Dispatchers.IO) {
            loginPreferences.saveToken(token)
        }
    }

    companion object {
        private val TAG = UserRepository::class.java.simpleName
        private const val LOGIN_ERROR_MESSAGE = "Login failed, please try again later."
        private const val REGISTER_ERROR_MESSAGE = "Register failed, please try again later."

        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, loginPreferences: LoginPreferences) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, loginPreferences)
            }.also { instance = it }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}