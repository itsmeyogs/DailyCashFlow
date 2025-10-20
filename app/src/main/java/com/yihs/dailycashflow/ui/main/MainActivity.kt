package com.yihs.dailycashflow.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivityMainBinding
import com.yihs.dailycashflow.ui.auth.AuthViewModel
import com.yihs.dailycashflow.ui.auth.LoginActivity
import com.yihs.dailycashflow.ui.category.CategoryFragment
import com.yihs.dailycashflow.ui.home.HomeFragment
import com.yihs.dailycashflow.ui.profile.ProfileFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSession().observe(this){ session ->
            Log.d("check session", "get Session $session")
            if(session.token.isEmpty() || session.token == ""){
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                setUpUI(savedInstanceState)
                setUpNavigationBar()
            }
        }
    }

    private fun setUpUI(savedInstanceState: Bundle?){
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        if(savedInstanceState == null){
            showFragment(HomeFragment())
        }
    }

    private fun setUpNavigationBar(){

        binding.bottomNavigation.setOnItemSelectedListener { menu ->
            var selectedFragment : Fragment? = null
            when(menu.itemId){
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_category -> selectedFragment = CategoryFragment()
                R.id.navigation_profile -> selectedFragment = ProfileFragment()
            }

            if(selectedFragment != null){
                showFragment(selectedFragment)
            }

            true
        }

    }


    private fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}