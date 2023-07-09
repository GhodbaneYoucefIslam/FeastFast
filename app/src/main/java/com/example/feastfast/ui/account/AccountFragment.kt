package com.example.feastfast.ui.account

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentAccountBinding
import com.example.feastfast.util.url
import java.util.*

class AccountFragment : Fragment() {

    var binding : FragmentAccountBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)


        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        // Check if the user is not logged in
        val navController = findNavController()
        val isLoggedIn = pref.getBoolean("connected", false)
        if (! isLoggedIn) {
            navController.navigate(R.id.notLoginFragment)
        }
        val name = pref.getString("name","not specified ")
        val imagePath = pref.getString("profile_image","not specified")
        setUp(name)

        Glide.with(requireActivity()).load(url +imagePath) . into(binding!!.pictureAccount)

        binding!!.logOut.setOnClickListener {
            pref.edit {
                clear()
            }

            navController.navigate(R.id.notLoginFragment)
        }




    }

    private fun setUp(name : String?){
        binding!!.profileName.text= name

        binding!!.cardView.setOnClickListener {
            // as per defined in your FragmentContainerView

            it.findNavController().navigate(R.id.action_navigation_account_to_personalinfoFragment)

        }
        binding!!.cardView2.setOnClickListener {
            // as per defined in your FragmentContainerView

            it.findNavController().navigate(R.id.action_navigation_account_to_favoriteFragment)

        }
        binding!!.cardView5.setOnClickListener {
            // as per defined in your FragmentContainerView

            it.findNavController().navigate(R.id.action_navigation_account_to_aboutAppFragment)

        }

        binding!!.cardView3.setOnClickListener {
            showChangeLang()
        }
    }


    private fun showChangeLang() {

        val listItems = arrayOf("french", "English")

        val mBuilder = AlertDialog.Builder(requireActivity())
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialog, which ->
            if (which == 0) {
                setLocate("frm")
                recreate(requireActivity())
            } else if (which == 1) {
                setLocate("en")
                recreate(requireActivity())
            }

            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()

    }

    private fun setLocate(Lang: String?) {

        val locale = Locale(Lang!!)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        requireActivity(). resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
        val editor = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        editor.edit {
            putString("My_Lang", Lang)

        }

    }




}