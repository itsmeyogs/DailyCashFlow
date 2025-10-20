package com.yihs.dailycashflow.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.data.ResultForm
import com.yihs.dailycashflow.databinding.ActivityRegisterBinding
import com.yihs.dailycashflow.utils.Constant
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

        onSignUpClicked()
        onLoginClicked()

        lifecycleScope.launch {
            viewModel.registerState.collect { result->
                when(result) {
                    is ResultForm.Idle -> {
                        showLoading(false)
                    }
                    is ResultForm.Loading -> {
                        showLoading(true)
                    }
                    is ResultForm.Success -> {
                        showLoading(false)
                        showSnackBar(
                            message = result.data.message,
                            actionText = getString(R.string.login),
                            action = { finish() }
                        )
                    }
                    is ResultForm.Error -> {
                        showLoading(false)
                        showSnackBar(result.message)
                    }
                }
            }
        }
    }


    private fun onSignUpClicked(){
        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                viewModel.register(name, email, password)
            }else{
                showSnackBar(Constant.FILL_ALL_FIELDS)
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


    private fun onLoginClicked(){
        binding.btnLogin.setOnClickListener {
            finish()
        }
    }
}