package com.example.gestionpizzeria.model

import kotlin.math.min

class Ganancia {
    private var mini: Int = 0
    private var porcion: Int=0
    private var pequenia: Int=0
    private var mediana: Int=0
    private  var familiar: Int=0
    private  var EG: Int=0
    private  var colaP: Int=0
    private  var colaM: Int=0
    private  var colaG: Int=0
    private  var gasto: String
    private var fecha=0


    constructor(porcion: Int,mini:Int, pequenia: Int, mediana: Int, familiar:Int, EG: Int, colaP:Int,colaM:Int,colaG:Int, gasto: String, fecha: Int){
        this.porcion=porcion
        this.mini= mini
        this.pequenia=pequenia
        this.mediana=mediana
        this.familiar=familiar
        this.EG=EG
        this.colaP=colaP
        this.colaM=colaM
        this.colaG=colaG
        this.gasto=gasto
        this.fecha=fecha
    }
    public fun getPorcion():Int{
        return this.porcion
    }
    public fun getMini():Int{
        return this.mini
    }
    public fun getPequenia():Int{
        return this.pequenia
    }
    public fun getMediana():Int{
        return this.mediana
    }
    public fun getFamiliar():Int{
        return this.familiar
    }
    public fun getEG():Int{
        return this.EG
    }
    public fun getColaP():Int{
        return this.colaP
    }
    public fun getColaM():Int{
        return this.colaM
    }
    public fun getColaG():Int{
        return this.colaG
    }
    public fun getGastos():String{
        return this.gasto
    }
    public fun getFecha():Int{
        return this.fecha
    }
}