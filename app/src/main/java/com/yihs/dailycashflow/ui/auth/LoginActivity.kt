package com.yihs.dailycashflow.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivityLoginBinding
import com.yihs.dailycashflow.ui.MainActivity
import com.yihs.dailycashflow.utils.Resource
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

        binding.apply {
            btnForgetPassword.setOnClickListener {
                onClickForgetPassword()
            }

            btnLogin.setOnClickListener {
                validateCredential()
            }

            btnRegisterNow.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }


        observeLoginState()


    }

    private fun validateCredential(){
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if(email.isNotEmpty() && password.isNotEmpty()){
            viewModel.login(email, password)
        }else{
            showSnackBar("Please fill all fields")
        }
    }


    private fun observeLoginState(){
        lifecycleScope.launch {
            viewModel.loginState.collect { resource ->
                when(resource){
                    is Resource.Idle -> {
                        binding.apply {
                            loadingIndicator.visibility = View.INVISIBLE
                            btnLogin.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Loading -> {
                        binding.apply {
                            loadingIndicator.visibility = View.VISIBLE
                            btnLogin.visibility = View.INVISIBLE
                        }
                    }
                    is Resource.Success -> {
                        binding.apply {
                            loadingIndicator.visibility = View.INVISIBLE
                            btnLogin.visibility = View.VISIBLE
                        }
                        viewModel.saveToken(resource.data.token!!)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is Resource.Error -> {
                        binding.apply {
                            loadingIndicator.visibility = View.INVISIBLE
                            btnLogin.visibility = View.VISIBLE
                        }
                        val message = resource.message

                        showSnackBar(message)
                    }
                }
            }
        }
    }


    private fun onClickForgetPassword(){
        showSnackBar("Coming Soon")
    }

    private fun showSnackBar(message: String){
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackBar.show()
    }
}