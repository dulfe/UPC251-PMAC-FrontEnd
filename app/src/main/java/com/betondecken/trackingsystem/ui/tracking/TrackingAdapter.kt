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

class TrackingAdapter(
    context: Context,
    trackingItems: MutableList<TrackingItem>,
    private val onDeleteClickListener: (position: Int) -> Unit,
    private val onItemClickListener: (position: Int) -> Unit
) : ArrayAdapter<TrackingItem>(context, 0, trackingItems) {

    private val mContext: Context = context
    private val mTrackingItems: MutableList<TrackingItem> = trackingItems

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView
        if (listItem == null) {
            listItem =
                LayoutInflater.from(mContext).inflate(R.layout.list_item_tracking, parent, false)
        }

        val currentItem = mTrackingItems[position]

        val codigoTextView = listItem?.findViewById<TextView>(R.id.text_codigo_seguimiento)
        codigoTextView?.text = currentItem.codigoDeSeguimiento

        val estadoTextView = listItem?.findViewById<TextView>(R.id.text_estado)
        estadoTextView?.text = currentItem.estado

        // Set color based on status
        val color = when (currentItem.estado) {
            "ENTREGADO" -> android.R.color.holo_blue_dark
            "EN_PROCESO" -> android.R.color.holo_green_dark
            "EN_FABRICACION" -> android.R.color.holo_orange_dark // Using orange for yellow-ish
            else -> android.R.color.holo_red_dark
        }
        estadoTextView?.setTextColor(ContextCompat.getColor(mContext, color))

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