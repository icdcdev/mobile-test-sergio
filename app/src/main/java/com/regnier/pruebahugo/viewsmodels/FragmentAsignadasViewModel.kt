package com.regnier.pruebahugo.viewsmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.regnier.pruebahugo.conexiones.Conexiones
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAsignadasViewModel : ViewModel() {

    val cargando = MutableLiveData<Boolean>()
    val conexiones = Conexiones()
    val mensaje = MutableLiveData<String>()
    val arregloOrdenes = MutableLiveData<ArrayList<OrdenesRecyclerModel>>()


    fun cargarOrdenes(){

        cargando.postValue(true)

        CoroutineScope(Dispatchers.IO).launch {

            val arrayResult = conexiones.ordenes()

            if (arrayResult.size > 0){

                arregloOrdenes.postValue(arrayResult)

                mensaje.postValue("OK")

            }else{

                mensaje.postValue("No contiene ordenes disponibles")
            }


            cargando.postValue(false)
        }


    }


}