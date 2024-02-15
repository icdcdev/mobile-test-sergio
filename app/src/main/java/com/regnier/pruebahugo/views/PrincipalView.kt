package com.regnier.pruebahugo.views

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.regnier.pruebahugo.R
import com.regnier.pruebahugo.databinding.PrincipalViewBinding
import com.regnier.pruebahugo.views.adapters.ViewPagerAdapter
import com.regnier.pruebahugo.views.fragments.FragmentAsignadasView
import com.regnier.pruebahugo.views.fragments.FragmentNoAsignadasView
import com.regnier.pruebahugo.viewsmodels.PrincipalViewModel
import java.util.Calendar

class PrincipalView : AppCompatActivity() {

    private lateinit var binding : PrincipalViewBinding
    private val principalViewModel : PrincipalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrincipalViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbPrincipal)

        if (supportActionBar != null){

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

        binding.tbPrincipal.navigationIcon = ResourcesCompat.getDrawable(resources,R.drawable.ring,theme)

        //binding.fabPrin.setImageResource(R.drawable.clock)

        val adapter = ViewPagerAdapter(this)
        binding.vp.adapter = adapter

        TabLayoutMediator(binding.tlPrin, binding.vp){ tab, position ->
            val seleccion = if (position == 0){
                "ASIGNADAS"
            }else{
                "NO ASIGNADAS"
            }

            tab.text = seleccion
        }.attach()

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                principalViewModel.etSearch.postValue(s.toString())

            }

        })

        binding.ivCalen.setOnClickListener{

            datePicker()
        }

        principalViewModel.etSearch.observe(this@PrincipalView){ orden ->

            val itemFrag = binding.vp.currentItem

            val fragment = supportFragmentManager.findFragmentByTag("f$itemFrag")

            if (fragment is FragmentAsignadasView){
                fragment.filterOrders(orden)
            }else if (fragment is FragmentNoAsignadasView){
                fragment.filterOrders(orden)
            }
        }

        principalViewModel.fecheSel.observe(this@PrincipalView){ fechaSel ->

            binding.tvFechaSel.text = fechaSel
        }

        binding.fabPrin.setOnClickListener{

            dialogoSalida()
        }

    }

    private fun datePicker(){

        val calendario = Calendar.getInstance()

        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, yearSel, monthOfYear, dayOfMonth ->

                principalViewModel.fecheSel.postValue("$dayOfMonth/${monthOfYear + 1}/$yearSel")
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()

    }

    private fun dialogoSalida(){

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Salir")
        builder.setMessage("¿Quiere salir de la aplicación?")
        builder.setPositiveButton("Aceptar"){_,_ ->

            finish()


        }

        builder.setNegativeButton("Cancelar"){_,_, ->


        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_principal,menu)

        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            dialogoSalida()
            return true
        }

        return super.onKeyDown(keyCode, event)

    }
}