package com.regnier.pruebahugo.models

class OrdenesRecyclerModel (
    val orderId: String,
    val serviceTypeId: Int,
    val serviceType: String,
    val model: String? = "",
    val startTime: String,
    val plates: String? = "",
    val pyramidColor: String,
    val pyramidNumber: Long,
    val orderStatusId: Long,
    val orderStatus: String,
    var asignada: Boolean,
    var file: String
)