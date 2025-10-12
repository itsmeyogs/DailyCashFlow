package com.yihs.dailycashflow.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivityRegisterBinding
import com.yihs.dailycashflow.utils.Resource
import com.yihs.dailycashflow.utils.showSnackBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        validateCredential()
        observeRegisterState()
        onClickLogin()
    }


    private fun validateCredential(){
        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                viewModel.register(name, email, password)
            }else{
                showSnackBar("Please fill all fields")
            }
        }
    }

    private fun observeRegisterState(){
        lifecycleScope.launch {
            viewModel.registerState.collect { resource ->
                when(resource){
                    is Resource.Idle -> {
                        showLoading(false)
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        //show message with action
                        showSnackBar(
                            message = resource.data.message,
                            actionText = "Login",
                            action = { finish() }
                        )


                    }
                    is Resource.Error -> {
                        showLoading(false)
                        //show message
                        showSnackBar(resource.message)
                    }
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean = false){
        binding.apply {
            if(isShow){
                loadingIndicator.visibility = View.VISIBLE
                btnSignUp.visibility = View.INVISIBLE
            }else{
                loadingIndicator.visibility = View.INVISIBLE
                btnSignUp.visibility = View.VISIBLE
            }
        }
    }


    private fun onClickLogin(){
        binding.btnLogin.setOnClickListener {
            finish()
        }
    }
}