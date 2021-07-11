package com.example.gestionpizzeria

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gestionpizzeria.UI.DatePickerFragment
import com.example.gestionpizzeria.control.conectionToBd
import com.example.gestionpizzeria.model.Ganancia
import kotlinx.android.synthetic.main.activity_consulta.*
import kotlinx.android.synthetic.main.activity_consulta.cantidadCG
import kotlinx.android.synthetic.main.activity_consulta.cantidadCM
import kotlinx.android.synthetic.main.activity_consulta.cantidadCP
import kotlinx.android.synthetic.main.activity_consulta.cantidadEG
import kotlinx.android.synthetic.main.activity_consulta.cantidadFam
import kotlinx.android.synthetic.main.activity_consulta.cantidadMe
import kotlinx.android.synthetic.main.activity_consulta.cantidadMini
import kotlinx.android.synthetic.main.activity_consulta.cantidadPeque
import kotlinx.android.synthetic.main.activity_consulta.cantidadPorcion
import kotlinx.android.synthetic.main.activity_consulta.totalitoConsultito
import kotlinx.android.synthetic.main.activity_consulta.txtTotalCG
import kotlinx.android.synthetic.main.activity_consulta.txtTotalCM
import kotlinx.android.synthetic.main.activity_consulta.txtTotalCP
import kotlinx.android.synthetic.main.activity_consulta.txtTotalEG
import kotlinx.android.synthetic.main.activity_consulta.txtTotalFamiliar
import kotlinx.android.synthetic.main.activity_consulta.txtTotalMediana
import kotlinx.android.synthetic.main.activity_consulta.txtTotalMini
import kotlinx.android.synthetic.main.activity_consulta.txtTotalPeque
import kotlinx.android.synthetic.main.activity_consulta.txtTotalPorcion
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest


class Consulta : AppCompatActivity() {

    private var context: Context =this

    private var dia:String=""
    private var mes=""
    private var anio=""
    private lateinit var fecha:String
    private var conectToBD = conectionToBd()
    //private lateinit var ganancia: Ganancia
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta)
        showDatePickerDialog()
        txtFecha.setOnClickListener{
            showDatePickerDialog()
        }
        btnBuscar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("Fecha en consultar","se:"+fecha)
                var condicion= conectToBD.existeRegistro(context, "fecha", fecha)
                if(condicion){
                    var ganancia=conectToBD.obtenerGanancia(context, fecha)
                    if( ganancia.getPorcion()!=0 || ganancia.getMini()!=0 || ganancia.getPequenia()!=0 ||
                            ganancia.getMediana()!=0 || ganancia.getFamiliar()!=0 || ganancia.getEG()!=0 ||
                            ganancia.getColaP()!=0 || ganancia.getColaM()!=0 || ganancia.getColaG()!=0 ||
                            !ganancia.getGastos().equals("0")){
                        Log.d("Ganancia 03: ","Gastos: "+ganancia.getGastos())
                        //aquiiiiiii llenar datos
                        cantidadPorcion.text= ""+ganancia.getPorcion()
                        cantidadMini.text= ""+ganancia.getMini()
                        cantidadPeque.text= ""+ganancia.getPequenia()
                        cantidadMe.text= ""+ganancia.getMediana()
                        cantidadFam.text= ""+ganancia.getFamiliar()
                        cantidadEG.text= ""+ganancia.getEG()
                        cantidadCP.text= ""+ganancia.getColaP()
                        cantidadCM.text= ""+ganancia.getColaM()
                        cantidadCG.text= ""+ganancia.getColaG()
                        totalGastos2.text=""+ganancia.getGastos()
                        val porcion = ganancia.getPorcion()
                        val mini = ganancia.getMini()
                        val peque = ganancia.getPequenia()
                        val media = ganancia.getMediana()
                        val fami = ganancia.getFamiliar()
                        val eg= ganancia.getEG()
                        val colap = ganancia.getColaP()
                        val colaM = ganancia.getColaM()
                        val colaG = ganancia.getColaG()
                        val gastos = ganancia.getGastos()
                        val cantiPorcion = (porcion.toDouble()* 1.25)
                        val cantimini = (mini.toDouble()*2.5)
                        val cantiPeque = (peque.toDouble()*4)
                        val cantimedia = (media.toDouble()*7.5)
                        val cantifami = (fami.toDouble()*10)
                        val cantieg = (eg.toDouble()*12)
                        val canticolP = (colap.toDouble()*0.5)
                        val cantcolM = (colaM.toDouble()*0.75)
                        val cantiGG = (colaG.toDouble()*1)
                        val total = ((porcion.toDouble()* 1.25) + (mini.toDouble()*2.5) + (peque.toDouble()*4) + (media.toDouble()*7.5)
                                + (fami.toDouble()*10) + (eg.toDouble()*12) + (colap.toDouble()*0.5) + (colaM.toDouble()*0.75) + (colaG.toDouble()*1)) - gastos.toDouble()
                        totalitoConsultito.setText(""+total)
                        txtTotalPorcion.setText("$"+cantiPorcion)
                        txtTotalMini.setText("$"+cantimini)
                        txtTotalPeque.setText("$"+cantiPeque)
                        txtTotalMediana.setText("$"+cantimedia)
                        txtTotalFamiliar.setText("$"+cantifami)
                        txtTotalEG.setText("$"+cantieg)
                        txtTotalCP.setText("$"+canticolP)
                        txtTotalCM.setText("$"+cantcolM)
                        txtTotalCG.setText("$"+cantiGG)

                    }else{
                        showAlertDialog()
                    }
                }else{
                    showAlertDialog()
                }

            }
        })

    }

    private fun showAlertDialog(){
        val dialogo1 = AlertDialog.Builder(context)
        dialogo1.setTitle("Error")
        dialogo1.setMessage("No existe registros en esta fecha")
        dialogo1.setCancelable(false)
        dialogo1.setPositiveButton("Aceptar", null)
        dialogo1.show()
    }
    private fun showDatePickerDialog(){

        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 because January is zero
            val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
            txtFecha.setText(selectedDate)
            txtfechaa.setText(selectedDate)

            dia=day.toString()
            if(dia.length==1){
                dia="0"+dia
            }
            mes=""+(month+1)
            if(mes.length==1){
                mes="0"+mes
            }
            anio=""+year
            fecha=anio+mes+dia
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }

}