package com.example.feastfast.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.feastfast.databinding.FragmentNotLoggedInPopUpBinding
import com.example.feastfast.ui.login.LoginActivity


class NotLoggedInPopUp : DialogFragment()  {

    lateinit var binding : FragmentNotLoggedInPopUpBinding
    lateinit var myContext : FragmentActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotLoggedInPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myContext = requireActivity()


        binding.buttonGoToAuth.setOnClickListener {
            val intent = Intent(myContext, LoginActivity::class.java)
            myContext.startActivity(intent)
            myContext.finish()
        }


    }
    
}