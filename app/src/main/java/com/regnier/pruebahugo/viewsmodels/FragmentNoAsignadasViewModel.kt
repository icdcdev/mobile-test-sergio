package com.regnier.pruebahugo.viewsmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.regnier.pruebahugo.conexiones.Conexiones
import com.regnier.pruebahugo.constants.Constants
import com.regnier.pruebahugo.constants.Constants.arrayAsig
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentNoAsignadasViewModel : ViewModel() {

    val cargando = MutableLiveData<Boolean>()
    private val conexiones = Conexiones()
    val mensaje = MutableLiveData<String>()
    val arregloOrdenes = MutableLiveData<ArrayList<OrdenesRecyclerModel>>()
    val muestraDialogo = MutableLiveData<Boolean>()
    val itemSel = MutableLiveData<OrdenesRecyclerModel>()
    val itemModificado = MutableLiveData<OrdenesRecyclerModel>()


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

    fun modificarItem(){

        val indice = arregloOrdenes.value!!.indexOf(itemSel.value)

        if (indice != -1){

            arregloOrdenes.value!![indice].asignada = true
            arregloOrdenes.value!![indice] = itemModificado.value!!

            //arrayAsig.clear()
            arrayAsig.add(arregloOrdenes.value!![indice])

        }


    }
}