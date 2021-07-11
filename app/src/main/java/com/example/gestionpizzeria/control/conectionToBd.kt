package com.example.gestionpizzeria.control

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.gestionpizzeria.BD.PizzQLiteOpenHelper
import com.example.gestionpizzeria.model.Ganancia
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class conectionToBd {
    public fun RegistrarFecha(context: Context, fecha: String){
        val conta = PizzQLiteOpenHelper(context, "Administracion", null, 1)
        val BaseDeDatos = conta.writableDatabase
        val registro = ContentValues()
        registro.put("fechaS", fecha)
        Log.d("FEHCA", fecha)
        BaseDeDatos.insert("fecha", null, registro)
        BaseDeDatos.close()
    }

    public fun BuscarPr(context: Context, campo: String, fechaS:String):String{
        var x="0"
        val conta = PizzQLiteOpenHelper(context, "Administracion", null, 1)
        val BaseDeDatos = conta.readableDatabase
        val c: Cursor = BaseDeDatos.rawQuery("SELECT ${campo} FROM ganancia where fecha=${fechaS}", null)
        if(c.moveToFirst()){
            x=c.getString(0)
        }else{
            Log.d("ERRRROR","BuscarPr")
        }
        c.close()
        BaseDeDatos.close()
        return x
    }

    public fun RegistrarGanacia(context: Context, porcion: String,  mini: String,pequenia:String, mediana: String, familiar: String,
            eg : String, colaP: String, colaM : String, colaG : String, gastos: String ,fecha : String){
        val conta = PizzQLiteOpenHelper(context, "Administracion", null, 1)
        val BaseDeDatos=conta.writableDatabase
        val registro = ContentValues()
        registro.put("porcion", porcion)
        registro.put("mini", mini)
        registro.put("pequenia", pequenia)
        registro.put("mediana", mediana)
        registro.put("familiar", familiar)
        registro.put("eg", eg)
        registro.put("colaP", colaP)
        registro.put("colaM", colaM)
        registro.put("colaG", colaG)
        registro.put("gastos",gastos)
        registro.put("fecha", fecha)
        BaseDeDatos.insert("ganancia", null, registro)
        BaseDeDatos.close()
    }
    public fun obtenerGanancia(context: Context, fecha:String): Ganancia{
        val contA = PizzQLiteOpenHelper(context, "Administracion", null, 1)
        val BaseDeDatoS = contA.readableDatabase
        val d: Cursor = BaseDeDatoS.rawQuery("SELECT * FROM ganancia where fecha=${fecha}", null)
        var p=0
        var m=0
        var peq=0
        var me=0
        var f=0
        var eg=0
        var cp=0
        var cm=0
        var cg=0
        var gastos=""
        var fecha=0
        if(d.moveToFirst()){
            p=d.getInt(0)
            m=d.getInt(1)
            peq=d.getInt(2)
            me=d.getInt(3)
            f=d.getInt(4)
            eg=d.getInt(5)
            cp=d.getInt(6)
            cm=d.getInt(7)
            cg=d.getInt(8)
            gastos=d.getString(9)
            fecha=d.getInt(10)
        }else{
            Log.d("ERROR","ERROR EN LA CONSULTA OBENER valores")
        }
        d.close()
        BaseDeDatoS.close()
        var retorno = Ganancia(p,m,peq,me,f,eg,cp,cm,cg,gastos,fecha)
        return retorno
    }
    public fun exportarBDToCSV(context: Context){
        val date = Date()
        var fecha=date.getTime()
        var guardo=false
        var path:String=""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var carpeta: File= File(""+path+"/ExportarSQLiteCSV")
        var archivo=carpeta.toString()+"/Ganancias_"+fecha+".csv"
        if(!carpeta.exists()){
            carpeta.mkdir()
            archivo=carpeta.toString()+"/Ganancias_"+fecha+".csv"
        }
        try{
            val fileWriter: FileWriter=FileWriter(archivo)
            val conta= PizzQLiteOpenHelper(context, "Administracion",null,1)
            val BaseDeDatos=conta.writableDatabase
            val d: Cursor = BaseDeDatos.rawQuery("SELECT * FROM ganancia ", null)
            Log.d("e","Hola entre ")
            if(d.moveToFirst()){
                fileWriter.append("Fecha")
                fileWriter.append("\t")
                fileWriter.append("Porcion")
                fileWriter.append("\t")
                fileWriter.append("Mini")
                fileWriter.append("\t")
                fileWriter.append("Pequeña")
                fileWriter.append("\t")
                fileWriter.append("Mediana")
                fileWriter.append("\t")
                fileWriter.append("Familiar")
                fileWriter.append("\t")
                fileWriter.append("Extra Grande")
                fileWriter.append("\t")
                fileWriter.append("Cola Pequeña")
                fileWriter.append("\t")
                fileWriter.append("Cola Mediana")
                fileWriter.append("\t")
                fileWriter.append("Cola Grande")
                fileWriter.append("\t")
                fileWriter.append("Gastos")
                fileWriter.append("\t")
                fileWriter.append("Total Ganancias")
                fileWriter.append("\n")

                do{
                    if(d.getInt(0)!=0 || d.getInt(1)!=0 ||d.getInt(2)!=0 ||
                        d.getInt(3)!=0 ||d.getInt(4)!=0 || d.getInt(5)!=0 ||
                        d.getInt(6)!=0 ||d.getInt(7)!=0 ||d.getInt(8)!=0 ||
                        !(d.getString(9).equals("0"))){
                        var fecha=obtenerFecha(""+d.getInt(10))
                        var porcion: Double = CalcularVendido(""+1.25, ""+d.getInt(0))
                        var mini: Double = CalcularVendido(""+2.5, ""+d.getInt(1))
                        var peque: Double = CalcularVendido(""+4, ""+d.getInt(2))
                        var mediana: Double = CalcularVendido(""+7.5, ""+d.getInt(3))
                        var familiar: Double = CalcularVendido(""+10, ""+d.getInt(4))
                        var eg: Double = CalcularVendido(""+12, ""+d.getInt(5))
                        var cp: Double = CalcularVendido(""+0.5, ""+d.getInt(6))
                        var cm: Double = CalcularVendido(""+0.75, ""+d.getInt(7))
                        var cg: Double = CalcularVendido(""+1, ""+d.getInt(8))
                        var gastos: String = d.getString(9)
                        var totalGanacias: Double = CalcularGanancia(porcion, mini, peque, mediana, familiar, eg, cp, cm, cg, gastos)
                        fileWriter.append(""+fecha)
                        fileWriter.append("\t")
                        fileWriter.append(""+porcion)
                        fileWriter.append("\t")
                        fileWriter.append(""+mini)
                        fileWriter.append("\t")
                        fileWriter.append(""+peque)
                        fileWriter.append("\t")
                        fileWriter.append(""+mediana)
                        fileWriter.append("\t")
                        fileWriter.append(""+familiar)
                        fileWriter.append("\t")
                        fileWriter.append(""+eg)
                        fileWriter.append("\t")
                        fileWriter.append(""+cp)
                        fileWriter.append("\t")
                        fileWriter.append(""+cm)
                        fileWriter.append("\t")
                        fileWriter.append(""+cg)
                        fileWriter.append("\t")
                        fileWriter.append(""+d.getString(9))
                        fileWriter.append("\t")
                        fileWriter.append(""+totalGanacias)
                        fileWriter.append("\n")
                    }
                }while(d.moveToNext())

                conta.close()
                fileWriter.close()
            }else{
                Log.d("Error:","No hay registros para exportar a CSV")
            }
        }catch ( io:Exception){
            Log.d("Excpetion","error al guardar::"+io.toString())
        }

    }

    public fun getFechaRegistro(context: Context):String{
        var flag=""
        val conta= PizzQLiteOpenHelper(context, "Administracion",null,1)
        val BaseDeDatos=conta.readableDatabase
        val c:Cursor= BaseDeDatos.rawQuery("SELECT fechaS FROM fecha ", null)
        if(c.moveToLast()){
            flag=c.getString(0)
        }else{
            Log.d("Error Consulta: ","Error en funcion obtener fecha")

        }
        c.close()
        BaseDeDatos.close()
        return flag
    }
    public fun existeRegistro(context: Context, campo:String ,fecha:String):Boolean{
        var flag=false//retorna false si no existe el regitro
        val conta= PizzQLiteOpenHelper(context, "Administracion",null,1)
        val BaseDeDatos=conta.readableDatabase
        val c:Cursor= BaseDeDatos.rawQuery("SELECT ${campo} FROM ganancia WHERE fecha=${fecha}", null)
        if(c.moveToFirst()){
            flag=true
        }
        c.close()
        BaseDeDatos.close()
        return flag
    }
    public fun existenGanancias(context: Context):Boolean{
        var flag=false
        val conta= PizzQLiteOpenHelper(context, "Administracion",null,1)
        val BaseDeDatos=conta.readableDatabase
        val c:Cursor= BaseDeDatos.rawQuery("SELECT * FROM ganancia ", null)
        if(c.moveToFirst()){
            flag=true
        }
        c.close()
        BaseDeDatos.close()
        return flag
    }
    public fun existeFecha(context: Context, fecha: String):Boolean{
        var flag=false
        val conta= PizzQLiteOpenHelper(context, "Administracion",null,1)
        val BaseDeDatos=conta.readableDatabase
        val c:Cursor= BaseDeDatos.rawQuery("SELECT fechaS FROM fecha WHERE fechaS=${fecha} ", null)
        if(c.moveToFirst()){
            flag=true
        }
        c.close()
        BaseDeDatos.close()
        return flag
    }
    public fun actualizarGanacia(context: Context, porcion: String, mini: String, pequenia:String, mediana: String, familiar: String,
                                eg : String, colaP: String, colaM : String, colaG : String, gastos: String ,fecha : String){
        val conta = PizzQLiteOpenHelper(context, "Administracion", null, 1)
        val BaseDeDatos=conta.writableDatabase
        val registro = ContentValues()
        registro.put("porcion", porcion)
        registro.put("mini", mini)
        registro.put("pequenia", pequenia)
        registro.put("mediana", mediana)
        registro.put("familiar", familiar)
        registro.put("eg", eg)
        registro.put("colaP", colaP)
        registro.put("colaM", colaM)
        registro.put("colaG", colaG)
        registro.put("gastos",gastos)
        registro.put("fecha", fecha)
        BaseDeDatos.update("ganancia", registro, "fecha=${fecha}",null)
        BaseDeDatos.close()
    }
    public fun limpiarDatos(context:Context, tabla:String, condicion: String?){
        val conta = PizzQLiteOpenHelper(context, "Administracion", null, 1)
        val BaseDeDatos=conta.writableDatabase
        BaseDeDatos.delete(tabla, condicion,null)
        BaseDeDatos.close()
    }
    private fun obtenerFecha(fecha: String): String{
        var anio: String=""
        var mes: String=""
        var dia:String=""
        var i:Int=0
        while(i < fecha.length){
            if(i<4){
                anio+=fecha[i]
            }
            if( i > 3 && i<6){
                mes+=fecha[i]
            }
            if( i > 5){
                dia+=fecha[i]
            }
            i++
        }
        return dia+"/"+mes+"/"+anio
    }
    private fun CalcularVendido(precio: String, cantidad: String): Double{
        var precioD = precio.toDouble()
        var cantidadD = cantidad.toDouble()
        val resultado = precioD*cantidadD
        return resultado
    }

    private fun CalcularGanancia(porcion: Double, mini: Double, pequenia: Double, mediana: Double, familiar: Double,
                                 eg: Double, cp: Double, cm: Double, cg: Double, gastos: String): Double{
        val gastosG = gastos.toDouble()
        val totalito: Double = (porcion+mini+pequenia+mediana+familiar+eg+cp+cm+cg)-gastosG
        return totalito
    }

}