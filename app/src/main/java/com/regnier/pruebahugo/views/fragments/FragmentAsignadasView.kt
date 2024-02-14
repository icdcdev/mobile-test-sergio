package com.regnier.pruebahugo.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.regnier.pruebahugo.databinding.FragmentAsignadasViewBinding
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import com.regnier.pruebahugo.utlis.ActualizaLista
import com.regnier.pruebahugo.utlis.ItemClickListenerRv
import com.regnier.pruebahugo.utlis.ItemEspacio
import com.regnier.pruebahugo.views.adapters.AdapterOrdenes
import com.regnier.pruebahugo.viewsmodels.FragmentAsignadasViewModel

class FragmentAsignadasView : Fragment(), ItemClickListenerRv {

    private lateinit var binding : FragmentAsignadasViewBinding
    private lateinit var adapterOrdenes : AdapterOrdenes
    private val fragmentAsignadasViewModel : FragmentAsignadasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAsignadasViewBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentAsignadasViewModel.cargarOrdenes()

        listeners()

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

                Toast.makeText(activity,resultado,Toast.LENGTH_SHORT).show()
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
    }

    override fun onItemClickRv(posicion: Int, ordenesModel: OrdenesRecyclerModel) {

        Toast.makeText(requireActivity(),"Seleccion ${ordenesModel.orderId}",Toast.LENGTH_SHORT).show()
    }


}