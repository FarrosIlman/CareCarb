package com.parrosz.carecarb.ui.onboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.parrosz.carecarb.R
import com.parrosz.carecarb.data.local.datastore.LoginPreferences
import com.parrosz.carecarb.databinding.ActivityOnboardingBinding
import com.parrosz.carecarb.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class OnboardingActivity : AppCompatActivity() {
    private val binding: ActivityOnboardingBinding by lazy {
        ActivityOnboardingBinding.inflate(layoutInflater)
    }

    private val onboardingViewModel: OnboardingViewModel by viewModels {
        OnboardingViewModel.OnboardingViewModelFactory.getInstance(LoginPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
            onboardingViewModel.setFirstTime(false)
            startActivity(intent)
        }
    }
}