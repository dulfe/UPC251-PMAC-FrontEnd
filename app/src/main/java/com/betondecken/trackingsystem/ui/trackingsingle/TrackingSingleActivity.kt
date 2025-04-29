package com.betondecken.trackingsystem.ui.trackingsingle

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.databinding.ActivityTrackingSingleBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class TrackingSingleActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel: TrackingSingleViewModel by viewModels()
    private lateinit var _map: GoogleMap
    private lateinit var binding: ActivityTrackingSingleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Crear el binding para la vista y establecer el contenido
        binding = ActivityTrackingSingleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjustar el padding de la vista principal para evitar que el contenido quede oculto detrás de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        // Agregar el toolbar como ActionBar
//        val toolbar = findViewById<MaterialToolbar>(R.id.my_toolbar)
//        setSupportActionBar(toolbar)
//
//        // Estas lineas son para mostrar el icono de la flecha de regreso en la barra de herramientas
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupComponents()
        setupListeners()
        observeState()
        observeEvents()

        // Retrieve the "codigoDeSeguimiento" extra from the Intent
        val codigoDeSeguimiento = intent.getStringExtra("codigoDeSeguimiento")

        // Use the value as needed
        if (codigoDeSeguimiento != null) {
            // For example, log it or pass it to the ViewModel
            viewModel.loadTrackingDetails(codigoDeSeguimiento)
        }
    }

    private fun setupComponents() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Agregar el toolbar como ActionBar
        val toolbar = findViewById<MaterialToolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        // Estas lineas son para mostrar el icono de la flecha de regreso en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupListeners() {
        // No hay listeners por ahora
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Actualizar la UI según el estado
                    binding.tvMessage.text = state.message
                    binding.tvMessage.visibility =
                        if (state.message != null) View.VISIBLE else View.GONE

                    if (state.isError || state.isLoading) {
                        binding.cardMessage.visibility = View.VISIBLE
                        binding.cardMap.visibility = View.GONE
                        binding.cardDetails.visibility = View.GONE

                        binding.progressBar.visibility =
                            if (state.isLoading) View.VISIBLE else View.GONE
                    } else {
                        binding.cardMessage.visibility = View.GONE
                        binding.cardMap.visibility = View.VISIBLE
                        binding.cardDetails.visibility = View.VISIBLE

                        if (state.trackingDetails != null) {
                            var data = state.trackingDetails
                            
                            // Mostrar valores
                            binding.txtDriver.text =
                                "${data.conductorNombres} ${data.conductorApellidos}"
                            binding.txtDriverPhone.text = data.conductorTelefono
                            binding.txtFactory.text = data.nombreDeLaFabrica
                            binding.txtDeliveryLocationName.text =
                                data.lugarDeEntrega
                            val formattedDate = data.fechaEstimadaDeEnvio
                                .format(
                                    DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()
                                    )
                                )
                            binding.txtEstimatedShipmentDate.text = formattedDate
                            binding.txtWeight.text = "${data.pesoEnKilos} kg"

                            var color: Int = android.R.color.holo_red_dark
                            var statusName: String = "DESCONOCIDO";
                            when (data.estado) {
                                // En Fabricacion
                                "P" -> {
                                    statusName = "EN FABRICACION"
                                    color = android.R.color.holo_orange_dark
                                }
                                // Enviado
                                "E" -> {
                                    statusName = "ENVIADO"
                                    color = android.R.color.holo_green_dark
                                }
                                // Entregado
                                "C" -> {
                                    statusName = "ENTREGADO"
                                    color = android.R.color.holo_blue_dark
                                }
                            }

                            binding.tvStatus.setBackgroundColor(
                                ContextCompat.getColor(
                                    this@TrackingSingleActivity,
                                    color
                                )
                            )
                            binding.tvStatus.text = statusName

                            if (::_map.isInitialized) {
                                // Crear la ubicación del marcador
                                val pos = LatLng(
                                    data.direccionDeEntregaLatitud, 
                                    data.direccionDeEntregaLongitud)
                                // Crear el marcador y mover la cámara
                                _map.addMarker(MarkerOptions()
                                    .position(pos)
                                    .title(data.lugarDeEntrega))
                                _map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))

                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        // No hay eventos por ahora
    }

    /**
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        _map = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        _map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        _map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Handle the Up button click
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed() // Navigate back using the back dispatcher
        return true
    }
}