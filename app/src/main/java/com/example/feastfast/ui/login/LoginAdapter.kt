package com.example.feastfast.ui.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter


class LoginAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {


    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            LoginFragment()
        } else SignUpFragment()
    }

    override fun getItemCount(): Int = 2

}
