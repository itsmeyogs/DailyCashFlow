package com.yihs.dailycashflow.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.databinding.ActivityRegisterBinding
import com.yihs.dailycashflow.utils.showSnackBar
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

        viewModel.registerState.observe(this){ result ->
            when(result){
                is Result.Loading ->{
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    showSnackBar(
                        message = result.data.message,
                        actionText = getString(R.string.login),
                        action = { finish() }
                    )
                }
                is Result.Error -> {
                    showLoading(false)
                    showSnackBar(result.message)
                }
                is Result.ErrorNetwork -> {
                    showLoading(false)
                    showSnackBar(getString(R.string.please_check_network))
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
                showSnackBar(getString(R.string.please_fill_all_fields))
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