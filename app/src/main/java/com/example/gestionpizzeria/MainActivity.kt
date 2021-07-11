package com.example.gestionpizzeria

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.gestionpizzeria.BD.PizzQLiteOpenHelper
import com.example.gestionpizzeria.control.Validacion
import com.example.gestionpizzeria.control.conectionToBd
import com.example.gestionpizzeria.model.Ganancia
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro_pizzas.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var conectionBD=conectionToBd()
    var context: Context =this
    //creados para exportar
    private var activity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pedirPermisos()
        btnRegistrar.setOnClickListener{
            var sa=  GregorianCalendar()
            var fechaSisToBDP=""+sa.get(Calendar.YEAR)
            var mes=sa.get(Calendar.MONTH)+1
            var mesStr =""+mes
            if(mesStr.length==1){
                mesStr="0"+mes
            }
            fechaSisToBDP=fechaSisToBDP+""+mesStr
            var diaStr=""+sa.get(Calendar.DATE)
            if(diaStr.length==1){
                diaStr="0"+diaStr
            }
            fechaSisToBDP+=diaStr
            var fechaActualSistema=Validacion.aEntero(fechaSisToBDP)
            var fechaActualEnBD=Validacion.aEntero(conectionBD.getFechaRegistro(context))
            if(fechaActualEnBD ==0){
                fechaActualEnBD=fechaActualSistema
            }
            Log.d("fecha sisvsBd","::"+fechaActualSistema+":::"+fechaActualEnBD)
            if( fechaActualSistema >= fechaActualEnBD){
                val intent:Intent = Intent(this, RegistroPizzas::class.java)
                startActivity(intent)//codigo de llamado a actividad
            }else{
                Toast.makeText(context, "Ya generó un registro el día de hoy",  Toast.LENGTH_LONG).show()
            }

        }
        btnConsultar.setOnClickListener{
            val intent:Intent = Intent(this, Consulta::class.java)
            startActivity(intent)//codigo de llamado a actividad
        }
        btnExportarBD.setOnClickListener {
            if(conectionBD.existenGanancias(context)){
                val dialogo1 = AlertDialog.Builder(context)
                dialogo1.setTitle("Advertencia")
                dialogo1.setMessage("Los datos serán exportados y posteiormente borrados de la base" +
                        "de datos,¿Esta seguro de esta acción?")
                dialogo1.setCancelable(false)
                dialogo1.setPositiveButton("Confirmar") { dialogo1, id -> exportar()}
                dialogo1.setNegativeButton("Cancelar") { dialogo1, id -> null }
                dialogo1.show()

            }else{
                val dialogo1 = AlertDialog.Builder(context)
                dialogo1.setTitle("Error")
                dialogo1.setMessage("No existen registros por exportar")
                dialogo1.setCancelable(false)
                dialogo1.setPositiveButton("Aceptar", null)
                dialogo1.show()
            }
        }
    }

    private fun exportar(){
        btnExportarBD.isEnabled= false
        btnRegistrar.isEnabled = false
        btnConsultar.isEnabled = false
        val thread= Thread(Runnable {
            conectionBD.exportarBDToCSV(context)
            conectionBD.limpiarDatos(context,"ganancia", null)
            conectionBD.limpiarDatos(context,"fecha", null)
        }).start()
        Toast.makeText(this, "Los datos fueron exportados correctamente.",  Toast.LENGTH_LONG).show()
        btnExportarBD.isEnabled = true
        btnRegistrar.isEnabled = true
        btnConsultar.isEnabled = true
    }
    private fun pedirPermisos(){
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ){
            var s= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(activity,s ,0)
        }
    }
}