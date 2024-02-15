package com.regnier.pruebahugo.conexiones

import android.util.Log
import com.regnier.pruebahugo.constants.Constants
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import com.regnier.pruebahugo.models.RespuestaOrdenesModel
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Conexiones {

    private fun conexionServidor():Retrofit{

        return Retrofit.Builder()
            .baseUrl(Constants.DOMINIO)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    suspend fun ordenes() : ArrayList<OrdenesRecyclerModel>{

        val arrayList = ArrayList<OrdenesRecyclerModel>()

        try{

            val call = conexionServidor().create(ApiServiceInterface::class.java)
                .ordenes()

            Log.d("Mensaje","Respuesta ${call.isSuccessful}")

            if (call.isSuccessful){

                if (call.body() != null){

                    //Log.d("Mensaje","Respuesta ${call.body()!!.message}")

                    if (call.body()!!.message.contains("successfully")){

                        val arrayOrdenes = call.body()!!.data

                        for (x in arrayOrdenes.indices){

                            arrayList.add(
                                OrdenesRecyclerModel(
                                    arrayOrdenes[x].orderId,
                                    arrayOrdenes[x].serviceTypeId,
                                    arrayOrdenes[x].serviceType,
                                    arrayOrdenes[x].model,
                                    arrayOrdenes[x].startTime,
                                    arrayOrdenes[x].plates,
                                    arrayOrdenes[x].pyramidColor,
                                    arrayOrdenes[x].pyramidNumber,
                                    arrayOrdenes[x].orderStatusId,
                                    arrayOrdenes[x].orderStatus,
                                    false,
                                    ""
                                )
                            )

                        }

                    }

                }

            }else{

                Log.d("Mensaje","Respuesta ${call.errorBody().toString()}")

            }

        }catch (exception : Exception){
            exception.printStackTrace()
        }

        return arrayList
    }
}