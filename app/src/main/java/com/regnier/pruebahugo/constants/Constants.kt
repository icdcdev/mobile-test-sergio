package com.regnier.pruebahugo.constants

import android.content.Context
import android.content.SharedPreferences
import com.regnier.pruebahugo.models.OrdenesRecyclerModel

object Constants {

    //https://5a5q7x88fb.execute-api.us-west-2.amazonaws.com/orders
    const val DOMINIO = "https://5a5q7x88fb.execute-api.us-west-2.amazonaws.com/"
    const val ORDENES = "orders"

    val arrayAsig = ArrayList<OrdenesRecyclerModel>()

    private lateinit var userData: SharedPreferences

    private const val USER_DATA: String = "ORDENES"

    fun init(context: Context){
        userData = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
    }

    fun getData(key: String, default:String): String?{

        return userData.getString(key,default)
    }

    fun saveData(key:String, data: String){
        val editor: SharedPreferences.Editor = userData.edit()
        editor.putString(key,data)
        editor.apply()
    }
}