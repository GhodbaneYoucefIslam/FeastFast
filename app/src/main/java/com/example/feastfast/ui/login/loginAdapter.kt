package com.example.feastfast.ui.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter


class loginAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {


    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            loginFragment()
        } else signupFragment()
    }

    override fun getItemCount(): Int = 2

}
