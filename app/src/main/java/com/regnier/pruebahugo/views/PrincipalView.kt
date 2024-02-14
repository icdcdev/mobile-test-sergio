package com.regnier.pruebahugo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.google.android.material.tabs.TabLayoutMediator
import com.regnier.pruebahugo.databinding.PrincipalViewBinding
import com.regnier.pruebahugo.views.adapters.ViewPagerAdapter

class PrincipalView : AppCompatActivity() {

    private lateinit var binding : PrincipalViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrincipalViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            return true
        }

        return super.onKeyDown(keyCode, event)

    }
}