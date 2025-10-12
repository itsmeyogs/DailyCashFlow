package com.yihs.dailycashflow.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            btnForgetPassword.setOnClickListener {
                onClickForgetPassword()
            }

            btnLogin.setOnClickListener {
                loadingIndicator.visibility = View.VISIBLE
                btnLogin.visibility = View.INVISIBLE
            }


        }


    }


    private fun onClickForgetPassword(){
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
    }
}