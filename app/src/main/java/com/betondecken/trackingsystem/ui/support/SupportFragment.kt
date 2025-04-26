package com.betondecken.trackingsystem.ui.support

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.betondecken.trackingsystem.databinding.FragmentSupportBinding
import androidx.core.net.toUri

class SupportFragment : Fragment() {

    private var _binding: FragmentSupportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(SupportViewModel::class.java)

        _binding = FragmentSupportBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.btnLaunchEmail.setOnClickListener {
            // Crear un Intent para abrir la aplicación de correo electrónico
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data =
                    "mailto:support@betondecken.com".toUri() // Only email apps should handle this
                putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                putExtra(Intent.EXTRA_TEXT, "Hola Soporte, tengo una pregunta...")
            }

            val context = it.context
            // Verificar si hay una aplicación de correo electrónico instalada
            if (emailIntent.resolveActivity(context.packageManager) != null) {
                startActivity(emailIntent)
            } else {
                // Si no hay ninguna aplicación de correo electrónico instalada, mostrar un mensaje
                Toast.makeText(context, "No email client found.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLaunchWhatsApp.setOnClickListener {
            val phoneNumber = "12345678901"

            // WhatsApp universal link URI
            val whatsappUri = Uri.parse("https://wa.me/$phoneNumber")

            val whatsappIntent = Intent(Intent.ACTION_VIEW, whatsappUri)

            // Obtener el contexto de la vista
            val context = it.context

            // Verificar si hay una aplicación de WhatsApp instalada
            if (whatsappIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(whatsappIntent)
            } else {
                // Si no hay ninguna aplicación de WhatsApp instalada, mostrar un mensaje
                Toast.makeText(context, "WhatsApp or a suitable app not found.", Toast.LENGTH_SHORT)
                    .show()
                // Esto es para abrir la Play Store y descargar WhatsApp
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "market://details?id=com.whatsapp".toUri()
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://play.google.com/store/apps/details?id=com.whatsapp".toUri()
                        )
                    )
                }
            }
        }

        binding.btnLaunchWebsite.setOnClickListener {
            val websiteUrl = "https://www.betondecken.com"
            val websiteIntent = Intent(Intent.ACTION_VIEW, websiteUrl.toUri())

            // Obtener el contexto de la vista
            val context = it.context

            // Verificar si hay un navegador web instalado
            if (websiteIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(websiteIntent)
            } else {
                // Si no hay ningún navegador web instalado, mostrar un mensaje
                Toast.makeText(context, "No web browser found.", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}