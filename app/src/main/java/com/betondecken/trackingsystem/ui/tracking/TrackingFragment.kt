package com.betondecken.trackingsystem.ui.tracking

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.databinding.FragmentTrackingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: TrackingAdapter
    private lateinit var trackingItems: MutableList<TrackingItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(TrackingViewModel::class.java)

        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textName
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data de ejemplo
        trackingItems = mutableListOf(
            TrackingItem("TRK123456789", "ENTREGADO"),
            TrackingItem("TRK987654321", "EN_PROCESO"),
            TrackingItem("TRK112233445", "EN_FABRICACION"),
            TrackingItem("TRK556677889", "CANCELADO"),
            TrackingItem("TRK998877665", "ENTREGADO")
        )

        // Creamos el adaptador y lo asignamos a la ListView
        adapter = TrackingAdapter(
            requireContext(),
            trackingItems,
            onDeleteClickListener = { position ->
                showDeleteConfirmationDialog(position)
            },
            onItemClickListener  = { position ->
                val clickedItem = trackingItems[position]
                Toast.makeText(requireContext(), "Clicked: ${clickedItem.codigoDeSeguimiento}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.lstTrackingList.adapter = adapter

    }

    private fun showDeleteConfirmationDialog(position: Int) {
        // Crear un diálogo de confirmación
        MaterialAlertDialogBuilder(
            requireContext())
            .setTitle(getString(R.string.tracking_listitem_delete_title))
            .setMessage(getString(R.string.tracking_listitem_delete_question))
            .setPositiveButton(getString(R.string.button_delete)) { dialog: DialogInterface, which: Int ->
                // Remove the item from the list
                trackingItems.removeAt(position)
                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(),
                    getString(R.string.tracking_listitem_delete_confirmation), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.button_cancel)) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}