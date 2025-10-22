package com.yihs.dailycashflow.ui.detail_transaction

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.yihs.dailycashflow.databinding.ActivityDetailTransactionBinding

class DetailTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //set up app bar and activate back button
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

       val transactionId = intent.getIntExtra(TRANSACTION_ID, -1)

        if(transactionId != -1){
            binding.tvTitle.text = "id : $transactionId"
        }

    }

    companion object{
        const val TRANSACTION_ID = "transaction_id"
    }
}