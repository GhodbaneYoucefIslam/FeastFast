package com.example.feastfast.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.feastfast.databinding.FragmentNotLoginBinding
import com.example.feastfast.ui.login.LoginActivity

class NotLoginFragment : Fragment() {




    var binding : FragmentNotLoginBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.loginRedirect.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(intent)
        }


    }

}