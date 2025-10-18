package com.yihs.dailycashflow.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.color.MaterialColors
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeViewModel by viewModel()

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

        setUpDropDown()
        setUpPieChart()


    }

    private fun setUpPieChart(){
        val pieChart = binding.pieChart

        //get entries from viewmodel
        val pieChartEntries = viewModel.pieChartEntries

        //create dataset pie from entries
        val dataset = PieDataSet(pieChartEntries,"")

        //set colors pie chart
        val colors = ArrayList<Int>()
        colors.add(MaterialColors.getColor(requireContext(), R.attr.colorIncomePieChart, Color.GREEN))
        colors.add(MaterialColors.getColor(requireContext(), R.attr.colorExpensePieChart, Color.RED))
        dataset.colors = colors

        val colorLabelPie = MaterialColors.getColor(requireContext(), R.attr.colorTextSpinner,
            Color.BLACK)
        val fontLabelPie = ResourcesCompat.getFont(requireContext(), R.font.poppins)

        //set show percent value
        pieChart.setUsePercentValues(true)

        // Pindahkan nilai (value) ke LUAR slice
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE)

        // Atur styling garis penghubung (value lines)
        dataset.valueLinePart1Length = 0.5f
        dataset.valueLinePart2Length = 0.5f
        dataset.valueLinePart1OffsetPercentage = 100f // Posisikan garis di tepi slice
        dataset.valueLineWidth = 1f
        dataset.valueLineColor = colorLabelPie // Samakan warna garis dengan teks

        // Format value untuk menampilkan LABEL dan PERSEN
        dataset.valueFormatter = object : PercentFormatter(pieChart) {
            override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                if (pieEntry == null) return ""
                // Dapatkan persen yang sudah diformat (misal: "80.0 %")
                val percent = super.getFormattedValue(value)

                // Gabungkan label dan persen (misal: "Income 80.0 %")
                return "${pieEntry.label} $percent"
            }
        }

        //change value text style to poppins
        dataset.valueTypeface = fontLabelPie
        //change value text size
        dataset.valueTextSize = 10f
        //change value text color
        dataset.valueTextColor = colorLabelPie

        //disable description in bottom right corner
        pieChart.description.isEnabled = false

        //disable legend
        pieChart.legend.isEnabled = false

        // Matikan EntryLabel bawaan (karena sudah kita gabung di value)
        pieChart.setDrawEntryLabels(false)

        //create pie data
        val pieData = PieData(dataset)
        pieChart.data = pieData
        //show with animate
        pieChart.animateY(1000)
    }





    private fun setUpDropDown(){
        val spinner = binding.dropdownRangeDateFilter
        val valuesDropDown = viewModel.filterRangeDateOptions

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



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}