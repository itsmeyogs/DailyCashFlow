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
import com.yihs.dailycashflow.data.model.SummaryResponse
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

        observeDataCashFlowSummary()
        setUpDropDownSummary()
        observeDataSummary()
        observeDataTransaction()
    }

    private fun observeDataCashFlowSummary(){
        viewModel.cashFlowSummaryTransactionState.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Loading -> {
                    showLoading(loadingCashFlow = true)
                }
                is Result.Success -> {
                    showLoading(loadingCashFlow = false)
                    showCashFlowSummaryTransaction(result.data)
                }
                is Result.Error -> {
                    showLoading(loadingCashFlow = false)
                    showSnackBar(result.message)
                }
                is Result.ErrorNetwork -> {
                    showLoading(loadingCashFlow = false)
                    showSnackBar(getString(R.string.please_check_network))
                }
            }
        }
    }




    private fun showCashFlowSummaryTransaction(data: SummaryResponse){
        binding.apply {
            val time = Helper.convertTimeStampToStringDate(data.endTime, Constant.DATE_WITHOUT_DAY_NAME_WITH_SHORT_MONTH_NAME)

            tvValueRemainingBalance.text = Helper.toRupiah(data.data.balance)
            tvDescRemainingBalance.text = getString(R.string.desc_remaining_balance, time)

            tvValueIncome.text = Helper.toRupiah(data.data.income)
            tvDescIncome.text = getString(R.string.desc_income, time)

            tvValueExpense.text = Helper.toRupiah(data.data.expense)
            tvDescExpense.text = getString(R.string.desc_expense, time)


        }
    }


    private fun observeDataSummary(){
        viewModel.summaryTransactionState.observe(viewLifecycleOwner){ result ->
            //set up 0 supaya jika error tetap tampil 0
            setUpPieChart(0, 0)
            when(result){
                is Result.Loading -> {
                    showLoading(loadingSummary = true)
                }
                is Result.Success -> {
                    val data = result.data.data

                    Log.d("data summary", "data: $data")

                    //submit data to pie chart
                    setUpPieChart(data.income, data.expense)
                    showLoading(loadingSummary = false)

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

            colors.add(Helper.getColorFromAttr(requireContext(), R.attr.colorExpensePieChart, Color.RED),)
            colors.add(Helper.getColorFromAttr(requireContext(), R.attr.colorIncomePieChart, Color.GREEN))

        }else{
            pieChartEntries.add(PieEntry(100f, ""))

            colors.add(Helper.getColorFromAttr(requireContext(), R.attr.colorDefaultPieChart, Color.LTGRAY))
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

        pieChart.invalidate()



    }

    private fun showDataSummary(income: Long, expense: Long){
        binding.apply {
            tvValueSummaryIncome.text = Helper.toRupiah(income)
            tvValueSummaryExpense.text = Helper.toRupiah(expense)
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
                    val data = result.data.data
                    //submit data to rv
                    homeAdapter.submitList(data)
                    //handle when data empty
                    handleDataTransactionEmpty(data)
                    showLoading(loadingTransaction = false)
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

    private fun showLoading(loadingCashFlow: Boolean = false, loadingSummary: Boolean = false,loadingTransaction: Boolean = false){
        binding.apply {
            if(loadingCashFlow){
                loadingIndicatorCardRemainingBalance.visibility = View.VISIBLE
                loadingIndicatorCardIncome.visibility = View.VISIBLE
                loadingIndicatorCardExpense.visibility = View.VISIBLE

                layoutCardRemainingBalance.visibility = View.INVISIBLE
                layoutCardIncome.visibility = View.INVISIBLE
                layoutCardExpense.visibility = View.INVISIBLE
            }else{
                loadingIndicatorCardRemainingBalance.visibility = View.GONE
                loadingIndicatorCardIncome.visibility = View.GONE
                loadingIndicatorCardExpense.visibility = View.GONE

                layoutCardRemainingBalance.visibility = View.VISIBLE
                layoutCardIncome.visibility = View.VISIBLE
                layoutCardExpense.visibility = View.VISIBLE
            }


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