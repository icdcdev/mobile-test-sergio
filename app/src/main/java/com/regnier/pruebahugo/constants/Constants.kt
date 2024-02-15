package com.regnier.pruebahugo.constants

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

     fun nombreImagen(activity: Activity): File {
        val nomTiempo: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "pruebaHugo_${nomTiempo}",
            ".jpg",
            dir
        )
    }

    fun nombreVideo(activity: Activity): File {
        val nomTiempo: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "pruebaHugo_${nomTiempo}",
            ".mp4",
            dir
        )
    }
}