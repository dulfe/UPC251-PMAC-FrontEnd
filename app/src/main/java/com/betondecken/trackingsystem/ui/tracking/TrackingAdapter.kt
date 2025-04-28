package com.betondecken.trackingsystem.ui.tracking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse

class TrackingAdapter(
    context: Context,
    trackingItems: MutableList<ResumenDeOrdenResponse>,
    private val onDeleteClickListener: (position: Int) -> Unit,
    private val onItemClickListener: (position: Int) -> Unit
) : ArrayAdapter<ResumenDeOrdenResponse>(context, 0, trackingItems) {

    private val _context: Context = context
    private val _items: MutableList<ResumenDeOrdenResponse> = trackingItems

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Cargar el layout de los elementos de la lista
        var listItem = convertView
        if (listItem == null) {
            listItem =
                LayoutInflater.from(_context).inflate(R.layout.list_item_tracking, parent, false)
        }

        // Obtener el elemento actual de la lista
        val currentItem = _items[position]

        // Asignar los valores a los TextViews
        val codigoTextView = listItem?.findViewById<TextView>(R.id.text_codigo_seguimiento)
        codigoTextView?.text = currentItem.codigoDeSeguimiento

        var color: Int = android.R.color.holo_red_dark
        var colorName: String = "DESCONOCIDO";
        when (currentItem.estado) {
            // En Fabricacion
            "P" -> {
                colorName = "EN FABRICACION"
                color = android.R.color.holo_orange_dark
            }
            // Enviado
            "E" -> {
                colorName = "ENVIADO"
                color = android.R.color.holo_green_dark
            }
            // Entregado
            "C" -> {
                colorName = "ENTREGADO"
                color = android.R.color.holo_blue_dark
            }
        }

        val estadoTextView = listItem?.findViewById<TextView>(R.id.text_estado)
        estadoTextView?.text = colorName
        estadoTextView?.setTextColor(ContextCompat.getColor(_context, color))

        // Enlazar eventos
        val deleteButton = listItem?.findViewById<ImageView>(R.id.button_delete)
        deleteButton?.setOnClickListener {
            onDeleteClickListener.invoke(position)
        }

        listItem?.setOnClickListener {
            onItemClickListener.invoke(position)
        }

        return listItem!!
    }
}