package com.example.gestionpizzeria.model

class Producto {

    private var nombre:String

    private var precio:String


    constructor(nombre: String, precio:String){
        this.nombre=nombre
        this.precio=precio
    }
    public fun getPrecio():String{
        return this.precio
    }
    public fun getNombre():String{
        return this.nombre
    }
}