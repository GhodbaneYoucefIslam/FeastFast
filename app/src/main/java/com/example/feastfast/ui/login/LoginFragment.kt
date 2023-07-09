package com.example.feastfast.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.feastfast.MainActivity
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentLoginBinding
import com.example.feastfast.models.LoginRequest
import com.example.feastfast.models.retrofit.Endpoint
import kotlinx.coroutines.*


class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    var binding : FragmentLoginBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding!!.root


        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding!!.showHidePasswordLogin.setOnClickListener {

            if ( binding!!.password.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // Show password
                binding!!.showHidePasswordLogin.setImageResource(R.drawable.icon_open_eye_password)

                binding!!.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding!!.showHidePasswordLogin.setImageResource(R.drawable.baseline_hide_source_24)

                // Hide password
                binding!!.password.transformationMethod = PasswordTransformationMethod.getInstance()
            }

        }

        // Initialize views
        emailEditText = binding!!.emailAdressInput
        passwordEditText = binding!!.password
        registerButton = binding!!.loginBtn


        // Register button click listener
        registerButton.setOnClickListener {
            // Perform registration
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()


            // Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val loginRequest = LoginRequest( email, password )
                // Call your registration API endpoint here and handle the response


                val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
                    requireActivity().runOnUiThread {
                        val errorMessage = "Error occurred: ${throwable.localizedMessage}"
                        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                    val response = Endpoint.createEndpoint().login(loginRequest)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!!
                            Toast.makeText(requireActivity(), "Welcome " + data.name, Toast.LENGTH_SHORT).show()

                            // save the token
                            val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            pref.edit {
                                putBoolean("connected",true)
                                putInt("idUser", data.id)
                                putString("name" ,data.name)
                                putString("email" ,data.email)
                                putString("phone" ,data.phone_number)
                                putString("profile_image" ,data.profile_picture)
                            }

                            val intent = Intent(requireActivity() ,MainActivity::class.java)
                            requireActivity().startActivity(intent)

                        } else {
                            Toast.makeText(requireActivity(), "login Request unsuccessfull!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }






    }


}
