package com.parrosz.carecarb.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.parrosz.carecarb.data.Result
import com.parrosz.carecarb.R
import com.parrosz.carecarb.databinding.ActivityRegisterBinding
import com.parrosz.carecarb.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModel.RegisterViewModelFactory.getInstance(this)
    }

    private fun setupView() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.btSignup.setOnClickListener {
            val firstName = binding.edFirstName.text
            val lastName = binding.edLastName.text
            val email = binding.edEmailLogin.text
            val password = binding.edPasswordLogin.text
            if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                registerViewModel.registerUser(
                    firstName.toString(),
                    lastName.toString(),
                    email.toString(),
                    password.toString()
                ).observe(this) {
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
                            Toast.makeText(this, getString(R.string.register_successful), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                if (firstName.isNullOrEmpty()) binding.edFirstName.error =
                    getString(R.string.name_cannot_empty)
                if (lastName.isNullOrEmpty()) binding.edLastName.error =
                    getString(R.string.name_cannot_empty)
                if (email.isNullOrEmpty()) binding.edEmailLogin.error =
                    getString(R.string.email_cannot_empty)
                if (email.isNullOrEmpty()) binding.edPasswordLogin.error =
                    getString(R.string.password_minimum)
            }
        }

        binding.signin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}