package com.yihs.dailycashflow.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivityMainBinding
import com.yihs.dailycashflow.ui.auth.LoginActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            viewModel.getSession().collect {
                if(it.token.isEmpty() || it.token == ""){
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    setUpUI()
                    showNameUser(it.name)
                    onClickLogOut()
                }
            }
        }


        setUpUI()
        onClickLogOut()


    }

    private fun setUpUI(){
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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