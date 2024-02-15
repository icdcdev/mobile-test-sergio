package com.regnier.pruebahugo.views.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.regnier.pruebahugo.R
import com.regnier.pruebahugo.constants.Constants.nombreImagen
import com.regnier.pruebahugo.constants.Constants.nombreVideo
import com.regnier.pruebahugo.databinding.FragmentAsignadasViewBinding
import com.regnier.pruebahugo.models.OrdenesRecyclerModel
import com.regnier.pruebahugo.utlis.ActualizaLista
import com.regnier.pruebahugo.utlis.ItemClickListenerRv
import com.regnier.pruebahugo.utlis.ItemEspacio
import com.regnier.pruebahugo.views.adapters.AdapterOrdenes
import com.regnier.pruebahugo.viewsmodels.FragmentAsignadasViewModel
import java.io.File
import java.io.IOException

class FragmentAsignadasView : Fragment(), ItemClickListenerRv {

    private lateinit var binding : FragmentAsignadasViewBinding
    private lateinit var adapterOrdenes : AdapterOrdenes
    private val fragmentAsignadasViewModel : FragmentAsignadasViewModel by viewModels()

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


        listeners()

    }

    override fun onResume() {
        super.onResume()

        fragmentAsignadasViewModel.cargarOrdenes()

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
                adapterOrdenes = AdapterOrdenes(requireActivity(),array,true,this)
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

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.CAMERA
                    ), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.CAMERA
                    ), 1
                )
            }
        } else {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S){

                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(), arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 2
                        )
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(), arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 2
                        )
                    }
                }

            }else {

                if (ordenesModel.file.isNotEmpty()){


                    val formato = ordenesModel.file.takeLast(3)

                    Log.d("MensajeFormato",formato)
                    if (formato == "jpg") {

                        dialogoFoto(ordenesModel.file)
                    }else{
                        dialogoVideo(ordenesModel.file)
                    }

                }else {

                    dialogoAsigna(ordenesModel)
                }
            }

        }
    }

    private fun dialogoAsigna(ordenAsignada : OrdenesRecyclerModel){

        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle("Selección")
        builder.setMessage("Selecciona una opción")
        builder.setPositiveButton("Foto"){_,_ ->

            tomaFoto(ordenAsignada)
        }

        builder.setNegativeButton("Video"){ _, _ ->

            tomarVideo(ordenAsignada)
        }

        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun tomaFoto(ordenAsignada: OrdenesRecyclerModel) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            val file: File? = try {
                nombreImagen(requireActivity())
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }
            file?.also {

                fragmentAsignadasViewModel.fileItem.postValue(file.absolutePath)
                fragmentAsignadasViewModel.ordenAsignado.postValue(ordenAsignada)

                val uri: Uri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.regnier.pruebahugo.android.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, 1)
            }
        }
    }

    private fun tomarVideo(ordenAsignada: OrdenesRecyclerModel) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            val file: File? = try {
                nombreVideo(requireActivity())
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }
            file?.also {

                fragmentAsignadasViewModel.fileItem.postValue(file.absolutePath)
                fragmentAsignadasViewModel.ordenAsignado.postValue(ordenAsignada)

                val videoURI: Uri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.regnier.pruebahugo.android.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
                startActivityForResult(intent, 2)
            }
        }
    }


    private fun dialogoFoto(photoPath: String?) {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogo_preview, null)
        builder.setView(dialogView)

        val imageView: ImageView = dialogView.findViewById(R.id.ivPreview)

        Glide.with(requireActivity())
            .load(photoPath)
            .into(imageView)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun dialogoVideo(photoPath: String?) {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogo_preview_video, null)
        builder.setView(dialogView)

        val videoView: VideoView = dialogView.findViewById(R.id.videoView)

        photoPath?.let {
            videoView.setVideoURI(it.toUri())
            videoView.start()
        }

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }


    fun filterOrders(order : String){

        adapterOrdenes.filter(order)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {

            fragmentAsignadasViewModel.actualizaFile()
            adapterOrdenes.updateItem(fragmentAsignadasViewModel.ordenAsignado.value!!)

        }else if (requestCode == 2 && resultCode == RESULT_OK) {

            fragmentAsignadasViewModel.actualizaFile()
            adapterOrdenes.updateItem(fragmentAsignadasViewModel.ordenAsignado.value!!)

        }

    }
}