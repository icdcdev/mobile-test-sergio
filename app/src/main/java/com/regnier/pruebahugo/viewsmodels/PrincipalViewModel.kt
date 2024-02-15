package com.regnier.pruebahugo.viewsmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrincipalViewModel : ViewModel() {

    val etSearch = MutableLiveData<String>()
    val fecheSel = MutableLiveData<String>()
}