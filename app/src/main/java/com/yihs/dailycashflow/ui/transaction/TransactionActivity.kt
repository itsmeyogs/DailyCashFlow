package com.yihs.dailycashflow.ui.transaction

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.ActivityTransactionBinding
import com.yihs.dailycashflow.utils.Constant
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    private val viewModel : TransactionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        setUpDropDownTypeTransaction()
        setUpDropDownFilterRangeTransaction()


    }


    private fun setUpDropDownFilterRangeTransaction(){
        val spinner = binding.dropdownFilterRangeTransaction
        val valuesDropDown = Constant.filterRangeDateOptions

        val adapterSpinner = ArrayAdapter(
            this,
            R.layout.spinner_selected_filter_range_transaction,
            valuesDropDown
        )
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_filter_range_transaction)
        spinner.adapter = adapterSpinner


        viewModel.selectedSpinnerFilterRange.observe(this){item ->
            val position = valuesDropDown.indexOf(item)
            if(position != -1) {
                Log.d("selected range changed", item.value)
                spinner.setSelection(position)
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = valuesDropDown[position]
                viewModel.changeSelectedSpinnerFilterRange(selectedValue)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setUpDropDownTypeTransaction(){
        val spinner = binding.dropdownTypeTransaction
        val valuesDropDown = Constant.filterTypeTransactionOptions

        val adapterSpinner = ArrayAdapter(
            this,
            R.layout.spinner_selected_type_transaction,
            valuesDropDown
        )
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_type_transaction)
        spinner.adapter = adapterSpinner

        //set default value
        viewModel.selectedSpinnerTypeTransaction.observe(this){ item ->
            val position = valuesDropDown.indexOf(item)
            if(position != -1) {
                Log.d("selected type changed", item.value)
                spinner.setSelection(position)
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = valuesDropDown[position]
                viewModel.changeSelectedSpinnerTypeTransaction(selectedValue)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }





}


