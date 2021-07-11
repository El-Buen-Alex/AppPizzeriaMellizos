package com.example.gestionpizzeria

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionpizzeria.control.Validacion
import com.example.gestionpizzeria.control.conectionToBd
import kotlinx.android.synthetic.main.activity_registro_diaaa.*
import java.util.*


class RegistroDiaaa : AppCompatActivity() {
    var conectToBd=conectionToBd()
    private var context: Context =this
    var fechaSisToBD=""
    var fechaBD=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_diaaa)
        //SISTEMA FECHA
        fechaBD=conectToBd.getFechaRegistro(context)

        var sa=  GregorianCalendar()
        fechaSisToBD=""+sa.get(Calendar.YEAR)
        var mes=sa.get(Calendar.MONTH)+1
        var mesStr =""+mes
        if(mesStr.length==1){
            mesStr="0"+mes
        }
        fechaSisToBD=fechaSisToBD+""+mesStr
        var diaStr=""+sa.get(Calendar.DATE)
        if(diaStr.length==1){
            diaStr="0"+diaStr
        }
        fechaSisToBD+=diaStr

        var gananciaHoy=conectToBd.obtenerGanancia(this, fechaBD)
        Log.d("Ganancia hoy",""+ gananciaHoy.getGastos())
        Log.d("Ganancia hoy",""+ gananciaHoy.getFecha())
        //aquiiiiiii llenar datos
        cantidadPorcion.text= ""+gananciaHoy.getPorcion()
        cantidadMini.text= ""+gananciaHoy.getMini()
        cantidadPeque.text= ""+gananciaHoy.getPequenia()
        cantidadMe.text= ""+gananciaHoy.getMediana()
        cantidadFam.text= ""+gananciaHoy.getFamiliar()
        cantidadEG.text= ""+gananciaHoy.getEG()
        cantidadCP.text= ""+gananciaHoy.getColaP()
        cantidadCM.text= ""+gananciaHoy.getColaM()
        cantidadCG.text= ""+gananciaHoy.getColaG()
        totalGastos.text=""+gananciaHoy.getGastos()


        var diaBD=""
        var mesBD=""
        var anioBD=""
        var x:Int=0
        var fechaGananciaa = ""+gananciaHoy.getFecha()
        while(x<fechaGananciaa.length){
            if(x<4){
                anioBD+=fechaGananciaa[x]
            }else if(x>3 && x<6){
                mesBD+=fechaGananciaa[x]
            }else if(x>5){
                diaBD+=fechaGananciaa[x]
            }
            x++
        } //aqui
        txtfechaa.text = diaBD+"/"+mesBD+"/"+anioBD

        val porcion = gananciaHoy.getPorcion()
        val mini = gananciaHoy.getMini()
        val peque = gananciaHoy.getPequenia()
        val media = gananciaHoy.getMediana()
        val fami = gananciaHoy.getFamiliar()
        val eg= gananciaHoy.getEG()
        val colap = gananciaHoy.getColaP()
        val colaM = gananciaHoy.getColaM()
        val colaG = gananciaHoy.getColaG()
        val gastos = gananciaHoy.getGastos()

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



        btnGuardar.setOnClickListener{
            val dialogo1 = AlertDialog.Builder(this)
            dialogo1.setTitle("Importante")
            dialogo1.setMessage("¿Está seguro que desea Guardar?")
            dialogo1.setCancelable(false)
            dialogo1.setPositiveButton("Confirmar") { dialogo1, id -> validarFecha(context); volver()}
            dialogo1.setNegativeButton("Cancelar") { dialogo1, id -> null }
            dialogo1.show()
            }
        }

        fun volver(){
            val intent = Intent().setClass(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        private fun validarFecha(context: Context){
            var fechaSistema=Validacion.aEntero(fechaSisToBD)
            var fechaBd=Validacion.aEntero(fechaBD)
            if(fechaSistema==fechaBd){
                val calendar = GregorianCalendar()
                calendar.add(Calendar.DATE,1)
                fechaBD=""+calendar.get(Calendar.YEAR)
                var mesStr=""+(calendar.get(Calendar.MONTH)+1)
                if(mesStr.length==1){
                    mesStr="0"+mesStr
                }

                fechaBD=fechaBD+""+mesStr
                var diaStr=""+(calendar.get(Calendar.DATE))
                if(diaStr.length==1){
                    diaStr="0"+diaStr
                }
                fechaBD=fechaBD+""+diaStr
                Log.d("fech Validar",""+fechaSistema+"   :"+fechaBD)
                conectToBd.RegistrarFecha(context, (fechaBD))
                conectToBd.RegistrarGanacia(context,"0","0","0","0","0","0","0","0","0","0",fechaBD)
               // fechaBd++
            }else if(fechaSistema>fechaBd ){
                Log.d("fech Validar",""+fechaSistema)
                conectToBd.RegistrarFecha(context, ""+(fechaSistema))
                conectToBd.RegistrarGanacia(context,"0","0","0","0","0","0","0","0","0","0","0"+fechaSistema)

            }


        }
    }
