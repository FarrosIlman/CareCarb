package com.parrosz.carecarb

import MainViewModel
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.parrosz.carecarb.data.local.datastore.LoginPreferences
import com.parrosz.carecarb.databinding.ActivityMainBinding
import com.parrosz.carecarb.ui.onboarding.OnboardingActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory.getInstance(
            this,
            LoginPreferences.getInstance(dataStore)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
    override fun onResume() {
        super.onResume()
        checkIfSessionValid()
    }

    private fun checkIfSessionValid() {
        mainViewModel.checkIfTokenAvailable().observe(this) {
            if (it == "null") {
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                setupView("Bearer $it")
            }
        }
    }

    private fun setupView(token: String) {
        /*mainViewModel.getStories(token).observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    val error = it.error
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    val data = it.data
                    if (data.isEmpty()) {
                        binding.tvNoStory.visibility = View.VISIBLE
                    } else {
                        binding.tvNoStory.visibility = View.INVISIBLE
                        binding.rvStories.apply {
                            adapter = StoriesAdapter(this@MainActivity, data)
                            layoutManager = LinearLayoutManager(this@MainActivity)
                        }
                    }
                }
            }
        }*/
    }
}