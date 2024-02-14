package com.regnier.pruebahugo.conexiones

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

        var arrayList = ArrayList<OrdenesRecyclerModel>()

        try{

            val call = conexionServidor().create(ApiServiceInterface::class.java)
                .ordenes()

            if (call.isSuccessful){

                if (call.body() != null){

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
                                    false
                                )
                            )

                        }

                    }

                }

            }

        }catch (exception : Exception){
            exception.printStackTrace()
        }

        return arrayList
    }
}