package com.regnier.pruebahugo.utlis

import com.regnier.pruebahugo.models.OrdenesRecyclerModel

interface ItemClickListenerRv {
    fun onItemClickRv(posicion : Int, ordenesModel:OrdenesRecyclerModel)
}