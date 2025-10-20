package com.yihs.dailycashflow.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.data.model.Summary
import com.yihs.dailycashflow.data.model.Transaction
import com.yihs.dailycashflow.databinding.FragmentHomeBinding
import com.yihs.dailycashflow.utils.Constant
import com.yihs.dailycashflow.utils.Helper
import com.yihs.dailycashflow.utils.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : HomeViewModel by viewModel()
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter = HomeAdapter()

        //set rv transaction to linear layout
        binding.rvTransactionHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = homeAdapter
        }

        //handle on click item transaction
        homeAdapter.onClickItem = { data ->
            handleClickItemTransaction(data)
        }


        setUpDropDownSummary()
        observeDataSummary()
        observeDataTransaction()
    }

    private fun observeDataSummary(){
        viewModel.summaryTransactionState.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading -> {
                    showLoading(loadingSummary = true)
                }
                is Result.Success -> {
                    showLoading(loadingSummary = false)
                    val data = result.data.data
                    //show data summary
                    showDataSummary(data)
                    //submit data to pie chart
                    setUpPieChart(data)
                    //handle when data zero
                    handleDataSummaryZero(data)
                }
                is Result.Error -> {
                    showLoading(loadingSummary = false)
                    showSnackBar(result.message)
                }
                is Result.ErrorNetwork -> {
                    showLoading(loadingSummary = false)
                    showSnackBar(getString(R.string.please_check_network))
                }
            }
        }
    }


    private fun handleDataSummaryZero(data : Summary){
        binding.apply {
            if(data.income > 0 || data.expense > 0){
                pieChartSummary.visibility = View.VISIBLE
                layoutLabelPieIncome.visibility = View.VISIBLE
                layoutLabelPieExpense.visibility = View.VISIBLE
            }else{
                pieChartSummary.visibility = View.GONE
                layoutLabelPieIncome.visibility = View.GONE
                layoutLabelPieExpense.visibility = View.GONE
            }
        }

    }




    private fun setUpPieChart(data: Summary){
        val pieChart = binding.pieChartSummary

        val income = data.income.toFloat()
        val expense = data.expense.toFloat()

        //show percent label manual, not in pie chart
        setUpLabelPieChart(income, expense)

        //convert data to pieEntry
        val pieChartEntries = listOf(
            //expense dulu agar income sebelah kiri di pie chart
            PieEntry(expense, resources.getString(R.string.expense)),
            PieEntry(income, resources.getString(R.string.income))
        )

        //set colors pie chart
        val colors = listOf(
            Helper.getColorFromAttr(requireContext(), R.attr.colorExpensePieChart, Color.RED),
            Helper.getColorFromAttr(requireContext(), R.attr.colorIncomePieChart, Color.GREEN)

        )

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
        pieChart.setHoleColor(Helper.getColorFromAttr(requireContext(), R.attr.backgroundColorCardSummary, Color.WHITE))

        //set hole size
        pieChart.holeRadius = 60f

        //disable entry label
        pieChart.setDrawEntryLabels(false)

        //disable legend
        pieChart.legend.isEnabled = false

        //create pie data
        val pieData = PieData(dataset)
        pieChart.data = pieData
        //show with animate
        pieChart.animateY(1000)
    }

    private fun showDataSummary(data: Summary){
        binding.apply {
            tvValueSummaryIncome.text = Helper.toRupiah(data.income)
            tvValueSummaryExpense.text = Helper.toRupiah(data.expense)
        }
    }

    private fun setUpLabelPieChart(income: Float, expense: Float){
        val total = income+expense
        val incomeRatio = income/total
        val expenseRatio = expense/total

        binding.apply {
            tvLabelPieChartIncome.text = String.format(resources.getString(R.string.label_pie_chart_income), Helper.toPercent(incomeRatio))
            tvLabelPieChartExpense.text = String.format(resources.getString(R.string.label_pie_chart_expense), Helper.toPercent(expenseRatio))
        }


    }


    private fun setUpDropDownSummary(){
        val spinner = binding.dropdownRangeDateFilter
        val valuesDropDown = Constant.filterRangeDateOptions

        val adapterSpinner = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_selected,
            valuesDropDown
        )
        adapterSpinner.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        spinner.adapter = adapterSpinner

        //set default value dropdown to daily
        viewModel.selectedFilterRangeDate.observe(viewLifecycleOwner){ item ->
            val position = valuesDropDown.indexOf(item)
            if(position != -1){
                Log.d("selected changed", item.value)
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
                viewModel.changeSelectedFilterRangeDate(selectedValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }


    private fun observeDataTransaction(){
        viewModel.transactionHistoryState.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading -> {
                    showLoading(loadingTransaction = true)
                }
                is Result.Success -> {
                    showLoading(loadingTransaction = false)
                    val data = result.data.data
                    //submit data to rv
                    homeAdapter.submitList(data)
                    //handle when data empty
                    handleDataTransactionEmpty(data)
                }
                is Result.Error -> {
                    showLoading(loadingTransaction = false)
                    showSnackBar(result.message)
                }
                is Result.ErrorNetwork -> {
                    showLoading(loadingTransaction = false)
                    showSnackBar(getString(R.string.please_check_network))
                }
            }
        }
    }


    private fun handleDataTransactionEmpty(data : List<Transaction>){
        binding.apply {
            if(data.isEmpty()){
                tvEmptyTransaction.visibility = View.VISIBLE
                rvTransactionHistory.visibility = View.GONE
            }else{
                tvEmptyTransaction.visibility = View.GONE
                rvTransactionHistory.visibility = View.VISIBLE
            }
        }
    }


    private fun handleClickItemTransaction(item: Transaction){
        showSnackBar("clicked item ${item.id}")

    }

    private fun showLoading(loadingSummary: Boolean = false,loadingTransaction: Boolean = false){
        binding.apply {
            if(loadingSummary){
                loadingIndicatorCardSummary.visibility = View.VISIBLE
                layoutCardSummary.visibility = View.INVISIBLE
            }else{
                loadingIndicatorCardSummary.visibility = View.GONE
                layoutCardSummary.visibility = View.VISIBLE
            }

            //loading transaction history
            if(loadingTransaction){
                loadingIndicatorTransactionHistory.visibility = View.VISIBLE
                rvTransactionHistory.visibility = View.GONE
            }else{
                loadingIndicatorTransactionHistory.visibility = View.GONE
                rvTransactionHistory.visibility = View.VISIBLE
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}