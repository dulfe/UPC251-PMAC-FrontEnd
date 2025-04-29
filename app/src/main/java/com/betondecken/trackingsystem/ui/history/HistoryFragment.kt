package com.betondecken.trackingsystem.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.betondecken.trackingsystem.databinding.FragmentHistoryBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModels()
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupComponents()
        setupListeners()
        observeState()
        observeEvents()

        viewModel.loadHistory()

        return root
    }

    private fun observeEvents() {
        // Nada, no hay eventos que escuchar
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.isError || state.isLoading) {
                        binding.cardMessage.visibility = View.VISIBLE
                        binding.cardChart.visibility = View.GONE

                        binding.progressBar.visibility =
                            if (state.isLoading) View.VISIBLE else View.GONE
                    } else {
                        binding.cardMessage.visibility = View.GONE
                        binding.cardChart.visibility = View.VISIBLE

                        if (state.history != null) {
                            setupChart(state.history)
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        // Nada, no hay eventos que procesar
    }

    private fun setupComponents() {
        // Nada, el grafico se inicializara cuando se recivan datos
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        setupChart()
//
//        super.onViewCreated(view, savedInstanceState)
//    }

    private fun setupChart(data: List<MapData>) {
        // Set up the chart here
        val chart = binding.chart
        chart.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        val entries = ArrayList<Entry>()
        for (i in 0..11) {
            val e = data[i]
            entries.add(Entry(i.toFloat(), e.value.toFloat()))
        }

        // Set up the chart data
        val dataSet = LineDataSet(entries, "Envios")
        dataSet.color = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)

        val linedata: LineData = LineData(dataSet)
        chart.setData(linedata)

        val xAxis = chart.xAxis
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getAxisLabel(
                value: Float,
                axis: com.github.mikephil.charting.components.AxisBase?
            ): String {
                return data[value.toInt()].label
            }
        }

        xAxis.setDrawLabels(true)
        xAxis.labelRotationAngle = 45f
        xAxis.setLabelCount(12, true)

        chart.description.text = "Envios de los ultimos 12 meses"

        // Show chart
        chart.invalidate()
    }

//    private fun setupChart() {
//        // Set up the chart here
//        val chart = binding.chart
//        chart.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
//        chart.setDrawGridBackground(false)
//        chart.setTouchEnabled(true)
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)
//        chart.setPinchZoom(true)
//
//        // add sample data
//        val entries = ArrayList<Entry>()
//        for (i in 0..100) {
//            entries.add(Entry(i.toFloat(), (Math.random() * 100).toFloat()))
//        }
//
//        // Set up the chart data
//        val dataSet = LineDataSet(entries, "Sample Data")
//        dataSet.color = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)
//
//        val data: LineData = LineData(dataSet)
//        chart.setData(data)
//
//        // Show chart
//        chart.invalidate()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}