package com.yihs.dailycashflow.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivitySplashBinding
import com.yihs.dailycashflow.ui.auth.AuthViewModel
import com.yihs.dailycashflow.ui.auth.LoginActivity
import com.yihs.dailycashflow.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            delay(1500)
            observeSession()
        }

    }


    private fun observeSession(){
        viewModel.getSession().observe(this){ session ->
            val intent = if(session.token.isEmpty() || session.token == ""){
                Intent(this@SplashActivity, LoginActivity::class.java)
            }else{
                Intent(this@SplashActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }

    }
}