package com.regnier.pruebahugo.conexiones

import com.regnier.pruebahugo.constants.Constants.ORDENES
import com.regnier.pruebahugo.models.RespuestaOrdenesModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceInterface {

    @POST(ORDENES)
    suspend fun ordenes() : Response<RespuestaOrdenesModel>
}