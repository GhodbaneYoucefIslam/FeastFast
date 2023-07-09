package com.example.feastfast.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.feastfast.MainActivity
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentPersonalinfoBinding

import com.example.feastfast.models.User
import com.example.feastfast.models.retrofit.Endpoint
import com.example.feastfast.util.url
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class PersonalInfoFragment : Fragment() {
    var binding: FragmentPersonalinfoBinding? = null
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalinfoBinding.inflate(inflater, container, false)
        val view = binding!!.root


        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        binding!!.changeprofile.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }


        binding!!.userName2.setText(pref.getString("name", "not specified "))
        binding!!.emailAdress.setText(pref.getString("email", "not specified "))
        binding!!.userPhone.setText(pref.getString("phone", "not specified "))
        val idUser = pref.getInt("idUser", 0)
        binding!!.saveChanges.setOnClickListener {


            editProfile(
                idUser,
                binding!!.userName2.text.toString(),
                binding!!.emailAdress.text.toString(),
                binding!!.userPhone.text.toString(),
                binding!!.passwordEdit.text.toString()
            )
        }
        val imagePath = pref.getString("profile_image","not specified");


        Glide.with(requireActivity()).load(url +imagePath) . into(binding!!.pictureProfile)

        binding!!.showHidePasswordEdit.setOnClickListener {

            if (binding!!.passwordEdit.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // Show password
                binding!!.showHidePasswordEdit.setImageResource(R.drawable.icon_open_eye_password)

                binding!!.passwordEdit.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding!!.showHidePasswordEdit.setImageResource(R.drawable.baseline_hide_source_24)

                // Hide password
                binding!!.passwordEdit.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }

        }
    }

    private fun editProfile(id: Int, name: String, email: String, phone: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                .show()
        } else {


            val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
                requireActivity().runOnUiThread {
                    val errorMessage = "Error occurred: ${throwable.localizedMessage}"
                    Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            var multipartImage: MultipartBody.Part? = null

            // create request body for image file
            if (imageUri != null) {
                val inputStream: InputStream? =
                    requireActivity().contentResolver.openInputStream(imageUri!!)
                val file = File(
                    requireActivity().cacheDir,
                    "temp_image"
                ) // Create a temporary file to store the image
                file.createNewFile()

                // Copy the contents of the input stream to the file
                val outputStream: OutputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                outputStream.close()
                inputStream?.close()

                val fileReqBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                multipartImage =
                    MultipartBody.Part.createFormData("image", file.name, fileReqBody)

                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                    val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                    val idUser = pref.getInt("idUser",0)


                    val userData = MultipartBody.Part
                        .createFormData(
                            "user_data",
                            "{\"id\":$idUser,\"name\" : \"$name\" ,\"email\":\"$email\" , \"phone_number\":\"$phone\",\"password\":\"$pass\"}"
                        )

                    val response = Endpoint.createEndpoint().editProfile(userData , multipartImage)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!! as User
                            Toast.makeText(
                                requireActivity(),
                                " hi  " + data.name,
                                Toast.LENGTH_SHORT
                            ).show()
                            // save the token
                            pref.edit {
                                putBoolean("connected",true)
                                putInt("idUser", data.id)
                                putString("name" ,data.name)
                                putString("email" ,data.email)
                                putString("phone" ,data.phone_number)
                                putString("profile_image" ,data.profile_picture)
                            }

                            val intent = Intent(requireActivity() , MainActivity::class.java)
                            //     intent.putExtra("user", data) // Pass the Restaurant object as an extra
                            requireActivity().startActivity(intent)
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "register Request unsuccessful!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }





        }
}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding!!.pictureProfile.setImageURI(imageUri)
        }

    }
}