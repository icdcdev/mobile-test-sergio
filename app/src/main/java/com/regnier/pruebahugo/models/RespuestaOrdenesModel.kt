package com.regnier.pruebahugo.models

data class RespuestaOrdenesModel (
    val message: String,
    val data: List<Datum>
)

data class Datum (
    val orderId: String,
    val serviceTypeId: Int,
    val serviceType: String,
    val model: String = "",
    val startTime: String,
    val plates: String = "",
    val pyramidColor: String,
    val pyramidNumber: Long,
    val orderStatusId: Long,
    val orderStatus: String
)
