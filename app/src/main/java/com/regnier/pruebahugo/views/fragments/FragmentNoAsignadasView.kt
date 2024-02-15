package com.regnier.pruebahugo.views.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.regnier.pruebahugo.databinding.FragmentAsignadasViewBinding
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import com.regnier.pruebahugo.utlis.ActualizaLista
import com.regnier.pruebahugo.utlis.ItemClickListenerRv
import com.regnier.pruebahugo.utlis.ItemEspacio
import com.regnier.pruebahugo.views.adapters.AdapterOrdenes
import com.regnier.pruebahugo.viewsmodels.FragmentNoAsignadasViewModel

class FragmentNoAsignadasView : Fragment(), ItemClickListenerRv {

    private lateinit var binding : FragmentAsignadasViewBinding
    private lateinit var adapterOrdenes : AdapterOrdenes
    private val fragmentAsignadasViewModel : FragmentNoAsignadasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAsignadasViewBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fragmentAsignadasViewModel.cargarOrdenes()


        listeners()

    }

    override fun onResume() {
        super.onResume()


    }

    private fun listeners() {

        fragmentAsignadasViewModel.cargando.observe(viewLifecycleOwner){ isCargando ->

            if (isCargando){

                binding.pbFragment.visibility = View.VISIBLE
                binding.rvAsig.visibility = View.GONE

            }else{
                binding.pbFragment.visibility = View.GONE
                binding.rvAsig.visibility = View.VISIBLE
            }

        }


        fragmentAsignadasViewModel.mensaje.observe(viewLifecycleOwner){ resultado ->

            if (resultado != "OK"){

                Toast.makeText(activity,resultado, Toast.LENGTH_SHORT).show()
            }

        }

        fragmentAsignadasViewModel.arregloOrdenes.observe(viewLifecycleOwner){array ->

            if (!::adapterOrdenes.isInitialized) {
                adapterOrdenes = AdapterOrdenes(requireActivity(),array,false,this)
                binding.rvAsig.adapter = adapterOrdenes

                val llm = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                binding.rvAsig.layoutManager = llm

                val itemDecoration = ItemEspacio()
                binding.rvAsig.addItemDecoration(itemDecoration)

            } else {
                val diffResult = DiffUtil.calculateDiff(ActualizaLista(adapterOrdenes.getLista(), array))
                adapterOrdenes.setLista(array)
                diffResult.dispatchUpdatesTo(adapterOrdenes)
            }



        }

        fragmentAsignadasViewModel.muestraDialogo.observe(viewLifecycleOwner){isVisible ->

            if (isVisible){
                dialogoAsigna()
            }
        }
    }

    private fun dialogoAsigna(){

        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle("Asignar")
        builder.setMessage("¿Quiere asignar esta orden?")
        builder.setPositiveButton("Aceptar"){_,_ ->

            fragmentAsignadasViewModel.modificarItem()

            adapterOrdenes.updateItem(fragmentAsignadasViewModel.itemSel.value!!)
            fragmentAsignadasViewModel.muestraDialogo.postValue(false)


        }

        builder.setNegativeButton("Cancelar"){_,_, ->

            fragmentAsignadasViewModel.muestraDialogo.postValue(false)
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onItemClickRv(posicion: Int, ordenesModel: OrdenesRecyclerModel) {

        //Toast.makeText(requireActivity(),"Seleccion ${ordenesModel.orderId}", Toast.LENGTH_SHORT).show()

        fragmentAsignadasViewModel.itemSel.postValue(ordenesModel)
        fragmentAsignadasViewModel.itemModificado.postValue(ordenesModel)

        fragmentAsignadasViewModel.muestraDialogo.postValue(true)
    }

    fun filterOrders(order : String){

        adapterOrdenes.filter(order)
    }

}