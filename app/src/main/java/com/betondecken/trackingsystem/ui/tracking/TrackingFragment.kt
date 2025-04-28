package com.betondecken.trackingsystem.ui.tracking

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.betondecken.trackingsystem.HomeActivity
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.databinding.FragmentTrackingBinding
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import com.betondecken.trackingsystem.ui.login.LoginEvent
import com.betondecken.trackingsystem.ui.register.RegisterActivity
import com.betondecken.trackingsystem.ui.trackingsingle.TrackingSingleActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.betondecken.trackingsystem.ui.tracking.TrackingAdapter

@AndroidEntryPoint
class TrackingFragment : Fragment() {

    private val viewModel: TrackingViewModel by viewModels()
    private var _binding: FragmentTrackingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: TrackingAdapter

    //private lateinit var trackingItems: MutableList<ResumenDeOrdenResponse>
    private lateinit var trackingItems: MutableList<ResumenDeOrdenResponse>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val homeViewModel = ViewModelProvider(this).get(TrackingViewModel::class.java)

        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListeners()
        observeState()
        observeEvents()

        return root
    }

    private fun setupListeners() {
        // Campos
        binding.txtTrackingCode.addTextChangedListener { text ->
            viewModel.onTrackingCodeInputChanged(text.toString())
        }

        // Botones
        binding.btnSearch.setOnClickListener {
            viewModel.onSearchClick()
        }
    }

    private fun observeState() {
        // Pattern estandar para observar el estado de la UI cuando se usa corutinas
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Cuando existan datos, lo mostramos (en este caso toda la lista)
                    trackingItems.clear()
                    trackingItems.addAll(state.recentSearchedOrders)
                    adapter.notifyDataSetChanged()

                    if (state.isLoadingRecentSearches) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.lstTrackingList.visibility = View.GONE
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.lstTrackingList.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        // Pattern estandar para observar el estado de los eventos cuando se usa corutinas
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is TrackingEvent.Error -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }

                        is TrackingEvent.IsValid -> {
                            val intent = Intent(context, TrackingSingleActivity::class.java)
                            intent.putExtra(
                                "codigoDeSeguimiento",
                                viewModel.uiState.value.trackingCode
                            )
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos la lista de elementos
        trackingItems = mutableListOf()
        // Creamos el adaptador y lo asignamos a la ListView
        adapter = TrackingAdapter(
            requireContext(),
            trackingItems,
            onDeleteClickListener = { position ->
                showDeleteConfirmationDialog(position)
            },
            onItemClickListener = { position ->
                val clickedItem = trackingItems[position]
                Toast.makeText(
                    requireContext(),
                    "Clicked: ${clickedItem.codigoDeSeguimiento}",
                    Toast.LENGTH_SHORT
                ).show()
                // start activity
                val intent = Intent(requireContext(), TrackingSingleActivity::class.java)
                intent.putExtra("codigoDeSeguimiento", clickedItem.codigoDeSeguimiento)
                startActivity(intent)
            }
        )
        binding.lstTrackingList.adapter = adapter

        // Carga inicial
        viewModel.loadRecentSearches()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        // Crear un diálogo de confirmación
        MaterialAlertDialogBuilder(
            requireContext()
        )
            .setTitle(getString(R.string.tracking_listitem_delete_title))
            .setMessage(getString(R.string.tracking_listitem_delete_question))
            .setPositiveButton(getString(R.string.button_delete)) { dialog: DialogInterface, which: Int ->
                // Remove the item from the list
                trackingItems.removeAt(position)
                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.tracking_listitem_delete_confirmation), Toast.LENGTH_SHORT
                ).show()
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