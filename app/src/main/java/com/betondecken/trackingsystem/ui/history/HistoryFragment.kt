package com.betondecken.trackingsystem.ui.history

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.betondecken.trackingsystem.databinding.FragmentHistoryBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class HistoryFragment : Fragment() {

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


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupChart()

        super.onViewCreated(view, savedInstanceState)
    }

    fun setupChart() {
        // Set up the chart here
        val chart = binding.chart
        chart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        // add sample data
        val entries = ArrayList<Entry>()
        for (i in 0..100) {
            entries.add(Entry(i.toFloat(), (Math.random() * 100).toFloat()))
        }

        // Set up the chart data
        val dataSet = LineDataSet(entries, "Sample Data")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.holo_blue_light)

        val data: LineData = LineData(dataSet)
        chart.setData(data)

        // Show chart
        chart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}