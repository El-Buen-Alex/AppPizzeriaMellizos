package com.example.gestionpizzeria.BD

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase.CursorFactory


class PizzQLiteOpenHelper(context: Context, name: String,factory: CursorFactory?, version: Int): SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table fecha( fechaS integer)")
        db.execSQL("create table ganancia(porcion integer, mini integer, pequenia integer, mediana integer, familiar integer, eg integer, colaP integer, colaM integer, colaG integer, gastos string, fecha integer)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}

