package com.example.gestionpizzeria

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionpizzeria.control.Validacion
import com.example.gestionpizzeria.control.conectionToBd
import com.example.gestionpizzeria.control.gestionComponentes
import com.example.gestionpizzeria.model.Producto
import kotlinx.android.synthetic.main.activity_registro_pizzas.*
import java.text.SimpleDateFormat
import java.util.*


class
RegistroPizzas : AppCompatActivity() {
    private var contextApp :Context =this
    private lateinit var porcionPr: Producto//instanciación de productos---
    private lateinit var miniPr: Producto
    private lateinit var pequeniaPr: Producto
    private lateinit var familiarPr: Producto
    private lateinit var medianaPr: Producto
    private lateinit var exGrandePr: Producto
    private lateinit var colaP:Producto
    private lateinit var colaM:Producto
    private lateinit var colaG:Producto
    private var conectToBd = conectionToBd()//encargada de la gestin de querys a la base de datos
    private var actualizarComponentes = gestionComponentes()//encargada de actualizar cada componente
    private var fechaSisToBD=""
    private fun setFechaSisToBD(fecha:String){
        this.fechaSisToBD=fecha
        ActualizarContadores()
    }
    private fun ActualizarContadores(){
        Log.d("gfechaAc",fechaSisToBD)
        contaPorcion.text=conectToBd.BuscarPr(contextApp, porcionPr.getNombre(),fechaSisToBD)
        contaMini.text=conectToBd.BuscarPr(contextApp,miniPr.getNombre(),fechaSisToBD)
        contaPequenia.text=conectToBd.BuscarPr(contextApp, pequeniaPr.getNombre(), fechaSisToBD)
        contaMediana.text=conectToBd.BuscarPr(contextApp, medianaPr.getNombre(),fechaSisToBD)
        contaFamiliar.text=conectToBd.BuscarPr(contextApp, familiarPr.getNombre(),fechaSisToBD)
        contaEG.text=conectToBd.BuscarPr(contextApp, exGrandePr.getNombre(),fechaSisToBD)
        contaColaP.text=conectToBd.BuscarPr(contextApp, colaP.getNombre(),fechaSisToBD)
        contaColaM.text=conectToBd.BuscarPr(contextApp, colaM.getNombre(),fechaSisToBD)
        contaColaG.text=conectToBd.BuscarPr(contextApp, colaG.getNombre(),fechaSisToBD)
        txtGastosI.text=conectToBd.BuscarPr(contextApp, "gastos",fechaSisToBD)
        numeroGastos.setText(""+conectToBd.BuscarPr(contextApp,"gastos",fechaSisToBD))
    }

     fun generarNuevoRegistro(fecha: String){
        conectToBd.RegistrarGanacia(contextApp,"0","0","0","0","0","0","0","0","0","0", fecha)
        fechaSisToBD=fecha
         conectToBd.RegistrarFecha(contextApp,fecha)
         ActualizarContadores()
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pizzas )

        //creando productos quemados
        porcionPr= Producto("Porcion","1.25")
        miniPr= Producto("Mini","2.5")
        pequeniaPr= Producto("pequenia","4")
        medianaPr=Producto("Mediana","7.5")
        familiarPr=Producto("Familiar","10")
        exGrandePr=Producto("eg","12")
        colaP=Producto("colaP", "0.5")
        colaM=Producto("colaM", "0.75")
        colaG=Producto("colaG", "1")

        //fecha del sistema

        val date = Date()
        val  df = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = df.format(date.getTime())
        txtfecha.setText(formattedDate)
        //CREANDO FECHA PARA BD
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
        fechaSisToBD=conectToBd.getFechaRegistro(contextApp)
        if(fechaSisToBD.equals("")){
            fechaSisToBD=fechaSisToBDP
            generarNuevoRegistro(fechaSisToBDP)
        }else{
            //obtener fecha
            var diaBD=""
            var mesBD=""
            var anioBD=""
            var x:Int=0
            while(x<fechaSisToBD.length && fechaSisToBD.length>0){
                if(x<4){
                    anioBD+=fechaSisToBD[x]
                }else if(x>3 && x<6){
                    mesBD+=fechaSisToBD[x]
                }else if(x>5){
                    diaBD+=fechaSisToBDP[x]
                }
                x++
            }
            if(!conectToBd.existeFecha(contextApp, fechaSisToBDP)) {
                var fg=GregorianCalendar()
                fg.add(Calendar.DATE,-1)
                var messs=""+(fg.get(Calendar.MONTH)+1)
                if(messs.length==1){
                    messs="0"+messs
                }
                var diaaa=""+fg.get(Calendar.DATE)
                if(diaaa.length==1){
                    diaaa="0"+diaaa
                }
                var fechaSistema:String= ""+fg.get(Calendar.YEAR)+messs+diaaa
                if(fechaSistema.equals(fechaSisToBD)){
                    val dialogo1 = AlertDialog.Builder(this)
                    dialogo1.setTitle("¡Importante!")
                    dialogo1.setMessage("Usted está accediendo a un registro antiguo, ¿desea continuar con el mismo o generar un nuevo registro?")
                    dialogo1.setCancelable(false)
                    Log.d("fechaSistemavsBd",""+fechaSisToBD+" : "+fechaSisToBDP)
                    dialogo1.setPositiveButton("Continuar Con el mismo") { dialogo1, id -> setFechaSisToBD(fechaSisToBD) }
                    dialogo1.setNegativeButton("Generar nuevo registro") { dialogo1, id -> generarNuevoRegistro(fechaSisToBDP) }
                    Log.d("fechas actuales","fecha sistema::"+fechaSisToBDP+"  fecha bd::"+fechaSisToBD)
                    dialogo1.show()
                }else{
                    fechaSisToBD=fechaSisToBDP
                    conectToBd.RegistrarFecha(contextApp, fechaSisToBD)
                    setFechaSisToBD(fechaSisToBD)
                    generarNuevoRegistro(fechaSisToBD)
                }
            }
            Log.d("FechaAhora","aja:::"+fechaSisToBD)
            ActualizarContadores()

        }
        //eventos Botones

        btnRPorcion.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                var s=Validacion.aEntero(contaPorcion.getText().toString())
                s=s+1
                Log.d("contador",""+s)
                conectToBd.actualizarGanacia(contextApp, ""+s, contaMini.getText().toString(),
                        contaPequenia.getText().toString(),contaMediana.getText().toString(),contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaPorcion.text=conectToBd.BuscarPr(contextApp, porcionPr.getNombre(),fechaSisToBD)
                horaPorcion.setText(actualizarComponentes.hora())

            }
        })
        btnRestarPorcion.setOnClickListener{
            var s=Validacion.aEntero(contaPorcion.getText().toString())
            s=s-1
            Log.d("contador",""+s)
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, ""+s, contaMini.getText().toString(),
                        contaPequenia.getText().toString(),contaMediana.getText().toString(),contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaPorcion.text=conectToBd.BuscarPr(contextApp, porcionPr.getNombre(),fechaSisToBD)
                horaPorcion.setText(actualizarComponentes.hora())
            }

        }
        btnRMini.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaMini.getText().toString())
                s=s+1
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),""+s, contaPequenia.getText().toString(),contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaMini.text=conectToBd.BuscarPr(contextApp,miniPr.getNombre(),fechaSisToBD)
                horaMini.setText(actualizarComponentes.hora())
            }
        })
        btnRestarMini.setOnClickListener{
            var s=Validacion.aEntero(contaMini.getText().toString())
            s=s-1
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),""+s, contaPequenia.getText().toString(),contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaMini.text=conectToBd.BuscarPr(contextApp,miniPr.getNombre(),fechaSisToBD)
                horaMini.setText(actualizarComponentes.hora())
            }
        }
        btnRPeque.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaPequenia.getText().toString())
                s=s+1
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(), ""+s,contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaPequenia.text=conectToBd.BuscarPr(contextApp,pequeniaPr.getNombre(),fechaSisToBD)
                horaPequenia.setText(actualizarComponentes.hora())
            }
        })
        btnRestarPeque.setOnClickListener{

                var s=Validacion.aEntero(contaPequenia.getText().toString())
                s=s-1
                if(s >=0){
                    conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(), ""+s,contaMediana.getText().toString(),
                            contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                            contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                    contaPequenia.text=conectToBd.BuscarPr(contextApp,pequeniaPr.getNombre(),fechaSisToBD)
                    horaPequenia.setText(actualizarComponentes.hora())
                }
        }
        btnRMediana.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaMediana.getText().toString())
                s=s+1
                Log.d("contador",""+s)
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString()
                        , ""+s, contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaMediana.text=conectToBd.BuscarPr(contextApp,medianaPr.getNombre(),fechaSisToBD)
                horaMediana.setText(actualizarComponentes.hora())
            }
        })

        btnRestarMediana.setOnClickListener{
            var s=Validacion.aEntero(contaMediana.getText().toString())
            s=s-1
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),
                        contaPequenia.getText().toString(), ""+s,
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaMediana.text=conectToBd.BuscarPr(contextApp,medianaPr.getNombre(),fechaSisToBD)
                horaMediana.setText(actualizarComponentes.hora())
            }
        }
        btnRFamiliar.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaFamiliar.getText().toString())
                s=s+1
                Log.d("contador",""+s)
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString()
                        ,contaPequenia.getText().toString(), contaMediana.getText().toString(),
                        ""+s, contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaFamiliar.text=conectToBd.BuscarPr(contextApp,familiarPr.getNombre(),fechaSisToBD)
                horaFamiliar.setText(actualizarComponentes.hora())
            }
        })

        btnRestarFamiliar.setOnClickListener{
            var s=Validacion.aEntero(contaFamiliar.getText().toString())
            s=s-1
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString() ,contaMediana.getText().toString(),
                        ""+s, contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaFamiliar.text=conectToBd.BuscarPr(contextApp,familiarPr.getNombre(),fechaSisToBD)
                horaFamiliar.setText(actualizarComponentes.hora())
            }
        }

        btnREG.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaEG.getText().toString())
                s=s+1
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString(),
                        contaMediana.getText().toString(), contaFamiliar.getText().toString(), ""+s,contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaEG.text=conectToBd.BuscarPr(contextApp,exGrandePr.getNombre(),fechaSisToBD)
                horaEG.setText(actualizarComponentes.hora())
            }
        })

        btnRestarEG.setOnClickListener{
            var s=Validacion.aEntero(contaEG.getText().toString())
            s=s-1
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString(),
                        contaMediana.getText().toString(), contaFamiliar.getText().toString(), ""+s,contaColaP.getText().toString(),
                        contaColaM.getText().toString(), contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaEG.text=conectToBd.BuscarPr(contextApp,exGrandePr.getNombre(),fechaSisToBD)
                horaEG.setText(actualizarComponentes.hora())
            }
        }

        btnGasto.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                if(!numeroGastos.getText().isEmpty()){
                    conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString(),
                            contaMediana.getText().toString(), contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                            contaColaM.getText().toString(), contaColaG.getText().toString(), numeroGastos.getText().toString(),fechaSisToBD)
                    txtGastosI.text=conectToBd.BuscarPr(contextApp, "gastos",fechaSisToBD)

                }else{
                    Toast.makeText(contextApp, "Ingrese un precio de GASTOS",  Toast.LENGTH_LONG).show()
                }
            }
        })

        btnColaG.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaColaG.getText().toString())
                s=s+1
                Log.d("contador",""+s)
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString() ,contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), ""+s, txtGastosI.getText().toString(),fechaSisToBD)
                contaColaG.text=conectToBd.BuscarPr(contextApp,colaG.getNombre(),fechaSisToBD)
                horaColaG.setText(actualizarComponentes.hora())
            }
        })

        btnRestarColaG.setOnClickListener{
            var s=Validacion.aEntero(contaColaG.getText().toString())
            s=s-1
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(), contaPequenia.getText().toString(),contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        contaColaM.getText().toString(), ""+s, txtGastosI.getText().toString(),fechaSisToBD)
                contaColaG.text=conectToBd.BuscarPr(contextApp,colaG.getNombre(),fechaSisToBD)
                horaColaG.setText(actualizarComponentes.hora())
            }
        }

        btnColaM.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaColaM.getText().toString())
                s=s+1
                Log.d("contador",""+s)
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(), contaPequenia.getText().toString(),contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        ""+s, contaColaM.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaColaM.text=conectToBd.BuscarPr(contextApp,colaM.getNombre(),fechaSisToBD)
                horaColaM.setText(actualizarComponentes.hora())
            }
        })

        btnRestarColaM.setOnClickListener{
            var s=Validacion.aEntero(contaColaM.getText().toString())
            s=s-1
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString(), contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),contaColaP.getText().toString(),
                        ""+s, contaColaM.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaColaM.text=conectToBd.BuscarPr(contextApp,colaM.getNombre(),fechaSisToBD)
                horaColaM.setText(actualizarComponentes.hora())
            }
        }

        btnColaP.setOnClickListener(object : OnClickListener{
            override fun onClick(v:View?){
                var s=Validacion.aEntero(contaColaP.getText().toString())
                s=s+1
                Log.d("contador",""+s)
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(), contaPequenia.getText().toString(),contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),""+s,
                        contaColaM.getText().toString() , contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaColaP.text=conectToBd.BuscarPr(contextApp,colaP.getNombre(),fechaSisToBD)
                horaColaP.setText(actualizarComponentes.hora())
            }
        })

        btnRestarColaP.setOnClickListener{
            var s=Validacion.aEntero(contaColaP.getText().toString())
            s=s-1
            if(s >=0){
                conectToBd.actualizarGanacia(contextApp, contaPorcion.getText().toString(),contaMini.getText().toString(),contaPequenia.getText().toString(), contaMediana.getText().toString(),
                        contaFamiliar.getText().toString(), contaEG.getText().toString(),""+s,
                        contaColaM.getText().toString() , contaColaG.getText().toString(), txtGastosI.getText().toString(),fechaSisToBD)
                contaColaP.text=conectToBd.BuscarPr(contextApp,colaP.getNombre(),fechaSisToBD)
                horaColaP.setText(actualizarComponentes.hora())
            }
        }
        btnGenerar.setOnClickListener{

                if( (   !(contaColaG.getText().toString()).equals("") ||
                            !(contaColaM.getText().toString()).equals("") || !(contaColaP.getText().toString()).equals("")
                            || !(contaPorcion.getText().toString()).equals("") || !(contaMediana.getText().toString()).equals("")
                            || !(contaFamiliar.getText().toString()).equals("") ||!(contaEG.getText().toString()).equals("")
                            || !(contaMini.getText().toString()).equals("")|| !(txtGastosI.getText().toString()).equals("")) || !(contaPequenia.getText().toString()).equals("") ) {
                    if(!(txtGastosI.getText().toString()).equals("")){
                        val intent:Intent = Intent(this, RegistroDiaaa::class.java)
                        startActivity(intent)
                        var convertPorcion=(contaPorcion.getText().toString())// llamada a metodo estatico
                        var convertMini=(contaMini.getText().toString())
                        var convertPequenia=contaPequenia.getText().toString()
                        var convertMediana=(contaMediana.getText().toString())
                        var convertFamiliar=(contaFamiliar.getText().toString())
                        var convertEG=(contaEG.getText().toString())
                        var convertColaP=(contaColaP.getText().toString())
                        var convertColaM=(contaColaM.getText().toString())
                        var convertColG=(contaColaG.getText().toString())
                        var fecha=conectToBd.getFechaRegistro(contextApp)
                        //recuperar vbalor gastos con whle tabla gastos
                        var gastos=txtGastosI.getText().toString()
                        //registrar a DB las ganancias
                        conectToBd.actualizarGanacia(contextApp, convertPorcion, convertMini, convertPequenia,convertMediana,
                            convertFamiliar, convertEG, convertColaP,convertColaM, convertColG, gastos, fecha)

                        }else{
                        Toast.makeText(contextApp, "Ingrese un valor en GASTOS.",  Toast.LENGTH_LONG).show()
                    }
                   }else{
                        Toast.makeText(contextApp, "No existen datos por Ingresar.",  Toast.LENGTH_LONG).show()
                   }
            }
        }

    }




