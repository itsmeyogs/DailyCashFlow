package com.yihs.dailycashflow.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.data.ResultForm
import com.yihs.dailycashflow.databinding.ActivityLoginBinding
import com.yihs.dailycashflow.ui.main.MainActivity
import com.yihs.dailycashflow.utils.Constant
import com.yihs.dailycashflow.utils.showSnackBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModel()

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


        onLoginClicked()
        onForgetPasswordClicked()
        onRegisterNowClicked()

        lifecycleScope.launch {
            viewModel.loginState.collect { result->
                when(result){
                    is ResultForm.Idle -> {
                        showLoading(false)
                    }
                    is ResultForm.Loading -> {
                        showLoading(true)
                    }

                    is ResultForm.Success -> {
                        showLoading(false)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is ResultForm.Error -> {
                        showLoading(false)
                        showSnackBar(result.message)
                    }
                }
            }
        }

    }

    private fun onLoginClicked(){
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty()){
                viewModel.login(email, password)
            }else{
                showSnackBar(Constant.FILL_ALL_FIELDS)
            }
        }
    }


    private fun onForgetPasswordClicked(){
        binding.btnForgetPassword.setOnClickListener {
            showSnackBar("Coming Soon")
        }
    }

    private fun onRegisterNowClicked(){
        binding.btnRegisterNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isShow: Boolean = false){
        binding.apply {
            if(isShow){
                loadingIndicator.visibility = View.VISIBLE
                btnLogin.visibility = View.INVISIBLE
            }else{
                loadingIndicator.visibility = View.INVISIBLE
                btnLogin.visibility = View.VISIBLE
            }
        }
    }

}