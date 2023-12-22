import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.parrosz.carecarb.data.di.Injection
import com.parrosz.carecarb.data.local.datastore.LoginPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val loginPreferences: LoginPreferences
) : ViewModel() {

    fun checkIfTokenAvailable(): LiveData<String> {
        return loginPreferences.getToken().asLiveData()
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            loginPreferences.deleteToken()
        }
    }

    class MainViewModelFactory private constructor(
        private val loginPreferences: LoginPreferences
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(loginPreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: MainViewModelFactory? = null

            fun getInstance(
                context: Context,
                loginPreferences: LoginPreferences
            ): MainViewModelFactory = instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(loginPreferences)
            }.also { instance = it }
        }
    }
}
