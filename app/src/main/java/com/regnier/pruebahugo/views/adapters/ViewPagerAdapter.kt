package com.regnier.pruebahugo.views.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.regnier.pruebahugo.views.fragments.FragmentAsignadasView

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position){
            0 -> FragmentAsignadasView()
            else -> FragmentAsignadasView()
        }

        return fragment
    }

}