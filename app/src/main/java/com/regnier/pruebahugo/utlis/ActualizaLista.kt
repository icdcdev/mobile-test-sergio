package com.regnier.pruebahugo.utlis

import androidx.recyclerview.widget.DiffUtil

class ActualizaLista<T>(
    private val viejaLista: List<T>,
    private val nuevaLista: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return viejaLista.size
    }

    override fun getNewListSize(): Int {
        return nuevaLista.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return viejaLista[oldItemPosition] == nuevaLista[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return viejaLista[oldItemPosition] == nuevaLista[newItemPosition]
    }
}