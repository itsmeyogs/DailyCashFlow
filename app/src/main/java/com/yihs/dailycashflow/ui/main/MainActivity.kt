package com.yihs.dailycashflow.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivityMainBinding
import com.yihs.dailycashflow.ui.auth.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeSession()
        onClickLogOut()


    }

    private fun observeSession(){
        viewModel.getSession().observe(this@MainActivity){
            if(it.token.isEmpty() || it.token == ""){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                showNameUser(it.name)
            }
        }
    }


    private fun showNameUser(name:String){
        binding.tvHello.text = getString(R.string.hello_main, name)
    }

    private fun onClickLogOut(){
        binding.btnLogOut.setOnClickListener {
            viewModel.removeSession()
        }
    }


}