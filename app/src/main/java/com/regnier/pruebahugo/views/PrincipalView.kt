package com.regnier.pruebahugo.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.regnier.pruebahugo.databinding.PrincipalViewBinding

class PrincipalView : AppCompatActivity() {

    private lateinit var binding : PrincipalViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrincipalViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            return true
        }

        return super.onKeyDown(keyCode, event)

    }
}