package com.regnier.pruebahugo.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.regnier.pruebahugo.R
import com.regnier.pruebahugo.databinding.ItemRecyclerBinding
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import com.regnier.pruebahugo.utlis.ItemClickListenerRv

class AdapterOrdenes(private val context: Context, private var arrayOrders : ArrayList<OrdenesRecyclerModel>, private val asignada : Boolean , private val itemClickRv: ItemClickListenerRv) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var listaCompleta = ArrayList<OrdenesRecyclerModel>()

    init {

        listaCompleta.addAll(arrayOrders)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(context))
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return arrayOrders.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val hold = holder as Holder

        if (asignada == arrayOrders[holder.adapterPosition].asignada) {

            hold.tvOrder.text = arrayOrders[holder.adapterPosition].serviceType
            hold.tvStatus.text = arrayOrders[holder.adapterPosition].orderStatus
            hold.tvIdOrder.text = arrayOrders[holder.adapterPosition].orderId
            hold.tvHorTime.text = arrayOrders[holder.adapterPosition].startTime
            hold.tvModel.text = arrayOrders[holder.adapterPosition].model
            hold.tvPlacas.text = arrayOrders[holder.adapterPosition].plates
            hold.tvColorPyramid.text = arrayOrders[holder.adapterPosition].pyramidColor
            hold.tvPyramidOrd.text = arrayOrders[holder.adapterPosition].pyramidNumber.toString()


            val statusColor = when (arrayOrders[holder.adapterPosition].orderStatusId.toInt()) {

                1 -> AppCompatResources.getDrawable(context, R.drawable.fondo_redondo_azul)
                2 -> AppCompatResources.getDrawable(context, R.drawable.fondo_redondo_morado)
                3 -> AppCompatResources.getDrawable(context, R.drawable.fondo_redondo_rojo)
                4 -> AppCompatResources.getDrawable(context, R.drawable.fondo_redondo_amarillo)
                else -> AppCompatResources.getDrawable(context, R.drawable.fondo_redondo_verde)


            }

            hold.tvStatus.background = statusColor

            hold.itemView.setOnClickListener {
                itemClickRv.onItemClickRv(holder.adapterPosition, arrayOrders[position])
            }

            if (arrayOrders[position].file.isNotEmpty()){
                hold.ivFotVid.visibility = View.VISIBLE
            }else{
                hold.ivFotVid.visibility = View.GONE
            }

        }
    }

    private class Holder(binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

        val tvOrder = binding.tvOrder
        val tvStatus = binding.tvStatus
        val tvIdOrder = binding.tvIdOrder
        val tvHorTime = binding.tvHorTime
        val tvModel = binding.tvModel
        val tvPlacas = binding.tvPlacas
        val tvColorPyramid = binding.tvColorPyramid
        val tvPyramidOrd = binding.tvPyramidOrd
        val ivFotVid = binding.ivFotVid


    }

    fun setLista(nuevaLista: ArrayList<OrdenesRecyclerModel>) {
        arrayOrders = nuevaLista
        listaCompleta.clear()
        listaCompleta.addAll(arrayOrders)
        notifyDataSetChanged()
    }

    fun getLista(): ArrayList<OrdenesRecyclerModel> {
        return arrayOrders
    }


    fun updateItem(item: OrdenesRecyclerModel) {
        val index = arrayOrders.indexOf(item)
        if (index != -1) {
            arrayOrders[index] = item

            if (asignada){
                notifyItemChanged(index)
            }else{
                arrayOrders.remove(arrayOrders[index])
                notifyDataSetChanged()
            }

            //arrayOrders.remove(arrayOrders[index])
            //notifyItemChanged(index)

        }
    }


    fun filter(filtro:String) {

        arrayOrders.clear()

        if (filtro.isEmpty()){

            arrayOrders.addAll(listaCompleta)
        }else{

            for (item in listaCompleta){

                if (item.orderId.lowercase().contains(filtro.lowercase())){
                    arrayOrders.add(item)
                }
            }
        }

        notifyDataSetChanged()


    }
}