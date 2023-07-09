package com.example.intentimpilicitexample3

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.intentimpilicitexample3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Constante que contiene el valor asignado al permiso de la app
    private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Abrir una pagina de internet
        binding.btnUrl.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse("https://github.com/")
            )

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Log.d("DEBUG", "Hay un problema para encontrar el navegador.")
            }
        }

        // Realizar llamada telefonica
        binding.btnCall.setOnClickListener {
            // Se comprueba si el permiso en cuestion esta concedido
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE // Manifest de Android
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permiso no concedido
                Log.d("DEBUG", "No esta concedido el permiso para llamar.")
                if (ActivityCompat.shouldShowRequestPermissionRationale( // Si el usuario ya ha rechazado al menos una vez (TRUE) se le da una explicacion
                        this, Manifest.permission.CALL_PHONE
                    )
                ) {
                    Log.d("DEBUG", "Se da una explicacion.")

                    // Creando cuadro de dialogo
                    val builderDialog = AlertDialog.Builder(this)
                    builderDialog.setTitle("Permiso para llamar")
                    builderDialog.setMessage("Puede resultar interesante indicar porque.")

                    // Las variables dialog y which en este caso no se utilizan
                    // Se podrian sustituir por _ cada una ({_, _ -> ...)
                    builderDialog.setPositiveButton(android.R.string.ok) { dialog, which ->
                        Log.d("DEBUG", "Se acepta y se vuelve a pedir permiso")
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.CALL_PHONE),
                            MY_PERMISSIONS_REQUEST_CALL_PHONE
                        )
                    }

                    builderDialog.setNeutralButton(android.R.string.cancel, null)
                    builderDialog.show()
                } else {
                    // No requiere explicacion, se pregunta por el permiso
                    Log.d("DEBUG", "Se acepta y se vuelve a pedir permiso")

                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        MY_PERMISSIONS_REQUEST_CALL_PHONE
                    )
                }
            } else {
                Log.d("DEBUG", "El permiso ya esta concedido")
                val intent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:965555555")
                )
                startActivity(intent)
            }
        } // Fin btnCallphone.setOnClickListener
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CALL_PHONE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("DEBUG", "Permiso concedido!!")
                } else {
                    Log.d("DEBUG", "Permiso rechazado!!")
                }
                return
            }
            else -> {
                Log.d("DEBUG", "Se pasa de los permisos.")
            }
        }
    }

}