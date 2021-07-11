package com.example.gestionpizzeria.control

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class gestionComponentes {

    public fun hora():String{
        val date = Date()
        val hourFormat: DateFormat = SimpleDateFormat("HH:mm:ss")
        return hourFormat.format(date)
    }
}