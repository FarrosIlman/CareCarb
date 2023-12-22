package com.parrosz.carecarb.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.parrosz.carecarb.MainActivity
import com.parrosz.carecarb.R
import com.parrosz.carecarb.data.Result
import com.parrosz.carecarb.databinding.ActivityLoginBinding
import com.parrosz.carecarb.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btMasukLogin.setOnClickListener {
            if (!binding.edEmailLogin.text.isNullOrEmpty() && !binding.edPasswordLogin.text.isNullOrEmpty()) {
                val email = binding.edEmailLogin.text.toString()
                val password = binding.edPasswordLogin.text.toString()
                loginViewModel.login(email, password).observe(this) {
                    when (it) {
                        is Result.Loading -> {
                            binding.porgressBar.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            binding.porgressBar.visibility = View.INVISIBLE
                            val error = it.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }

                        is Result.Success -> {
                            binding.porgressBar.visibility = View.INVISIBLE
                            val data = it.data
                            loginViewModel.saveToken(data.loginResult.token)
                            Log.d("LoginActivity", "Token: ${data.loginResult.token}")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                binding.edEmailLogin.error = resources.getString(R.string.email_cannot_empty)

                if (binding.edPasswordLogin.text.isNullOrEmpty()) {
                    binding.edPasswordLogin.error =
                        resources.getString(R.string.password_minimum)
                }
            }
        }

        binding.signup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

/*
    override fun onResume() {
        super.onResume()
        initialCheck()
    }

    private fun initialCheck() {
        *//*loginViewModel.observe(this) {
            if (it) {
                val intent = Intent(this, OnboardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }*//*
    }*/
}