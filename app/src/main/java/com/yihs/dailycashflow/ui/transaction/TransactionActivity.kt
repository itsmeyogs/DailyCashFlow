package com.yihs.dailycashflow.ui.transaction

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.databinding.ActivityTransactionBinding
import com.yihs.dailycashflow.utils.Constant
import com.yihs.dailycashflow.utils.Helper
import com.yihs.dailycashflow.utils.showSnackBar
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
        observeDataSummary()


    }


    private fun observeDataSummary(){

        viewModel.summaryTransactionState.observe(this){ result ->
            //set up 0 supaya jika error tetap tampil 0
            setUpPieChart(0, 0)
            when(result){
                is Result.Loading -> {
                    showLoadingSummary(true)
                }
                is Result.Success -> {
                    val data = result.data.data

                    Log.d("data summary", "data: $data")

                    //submit data to pie chart
                    setUpPieChart(data.income, data.expense)
                    showLoadingSummary(false)

                }
                is Result.Error -> {
                    showLoadingSummary(false)
                    showSnackBar(result.message)
                }
                is Result.ErrorNetwork -> {
                    showLoadingSummary(false)
                    showSnackBar(getString(R.string.please_check_network))
                }
            }
        }
    }


    private fun setUpPieChart(dataIncome: Long, dataExpense: Long){
        //show data summary
        showDataSummary(dataIncome, dataExpense)

        val pieChart = binding.pieChartSummary

        val income = dataIncome.toFloat()
        val expense = dataExpense.toFloat()

        //show percent label manual, not in pie chart
        setUpLabelPieChart(income, expense)

        //set value and colors
        val pieChartEntries = arrayListOf<PieEntry>()
        val colors = arrayListOf<Int>()

        if(income > 0f || expense > 0f){
            //expense dulu agar income sebelah kiri di pie chart
            pieChartEntries.add(PieEntry(expense, resources.getString(R.string.expense)))
            pieChartEntries.add(PieEntry(income, resources.getString(R.string.income)))

            colors.add(Helper.getColorFromAttr(this, R.attr.colorExpensePieChart, Color.RED),)
            colors.add(Helper.getColorFromAttr(this, R.attr.colorIncomePieChart, Color.GREEN))

        }else{
            pieChartEntries.add(PieEntry(100f, ""))

            colors.add(Helper.getColorFromAttr(this, R.attr.colorDefaultPieChart, Color.LTGRAY))
        }

        //create dataset pie from entries
        val dataset = PieDataSet(pieChartEntries,"")

        dataset.colors = colors

        //hide value
        dataset.setDrawValues(false)

        //disable description in bottom right corner
        pieChart.description.isEnabled = false

        //set space slice(pemisah antar slice)
        dataset.sliceSpace = 1f

        //enable hole in center
        pieChart.isDrawHoleEnabled = true

        //change color hole
        pieChart.setHoleColor(Helper.getColorFromAttr(this, R.attr.backgroundColorCardSummary, Color.WHITE))

        //set hole size
        pieChart.holeRadius = 60f

        //disable entry label
        pieChart.setDrawEntryLabels(false)

        //disable legend
        pieChart.legend.isEnabled = false

        //create pie data
        val pieData = PieData(dataset)
        pieChart.data = pieData

        pieChart.invalidate()



    }

    private fun showDataSummary(income: Long, expense: Long){
        val total = income+expense
        binding.apply {
            tvValueSummaryIncome.text = Helper.toRupiah(income)
            tvValueSummaryExpense.text = Helper.toRupiah(expense)
            tvValueSummaryTotal.text = Helper.toRupiah(total)
        }
    }

    private fun setUpLabelPieChart(income: Float, expense: Float){
        val total = income+expense
        val incomeRatio = income/total
        val expenseRatio = expense/total

        binding.apply {
            tvLabelPieChartIncome.text = getString(R.string.label_pie_chart_income, Helper.toPercent(incomeRatio))
            tvLabelPieChartExpense.text = getString(R.string.label_pie_chart_expense, Helper.toPercent(expenseRatio))
        }
    }

    private fun showLoadingSummary(isShow: Boolean){
        binding.apply {
            if(isShow){
                loadingIndicatorCardSummary.visibility = View.VISIBLE
                layoutCardSummary.visibility = View.INVISIBLE
            }else{
                loadingIndicatorCardSummary.visibility = View.GONE
                layoutCardSummary.visibility = View.VISIBLE
            }
        }
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


