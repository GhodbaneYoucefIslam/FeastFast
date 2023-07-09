package com.example.feastfast.ui.login

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.feastfast.MainActivity
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentSignupBinding
import com.example.feastfast.models.User
import com.example.feastfast.models.retrofit.Endpoint
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


class SignUpFragment : Fragment() {
    var binding : FragmentSignupBinding? = null

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var fullNameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private val pickImage = 100
    private val REQUEST_IMAGE_CAPTURE = 0
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        return binding!!.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding!!.profilePicture.setImageURI(imageUri)
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding!!.profilePicture.setImageBitmap(imageBitmap)
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity() , gso)

        binding!!.googleIcon.setOnClickListener {
            print("------------------------------------------")
            print("------------------------------------------")
            print("------------------------------------------")
            print("------------------------------------------")
            print("------------------------------------------")
            print("------------------------------------------")

            signInGoogle()
            print("------------------------------------------")
        }

        // Initialize views
        emailEditText = binding!!.emailAdressInput
        passwordEditText = binding!!.password
        fullNameEditText = binding!!.editTextTextPersonName
        phoneNumberEditText = binding!!.editTextPhone2
        registerButton = binding!!.registerBtn
        binding!!.addPhotoIcon.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding!!.showHidePassword.setOnClickListener {

            if ( binding!!.password.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // Show password
                binding!!.showHidePassword.setImageResource(R.drawable.icon_open_eye_password)

                binding!!.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding!!.showHidePassword.setImageResource(R.drawable.baseline_hide_source_24)

                // Hide password
                binding!!.password.transformationMethod = PasswordTransformationMethod.getInstance()
            }

        }
        binding!!.takePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_IMAGE_CAPTURE
                )
            }
        }


        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val fullName = fullNameEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
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



                       val userData = MultipartBody.Part
                        .createFormData(
                            "user_data",
                                "{\"name\" : \"$fullName\" ,\"email\":\"$email\" , \"phone_number\":\"$phoneNumber\",\"password\":\"$password\",\"registration_type\":\"Native\"}"
                        )

                        val response = Endpoint.createEndpoint().register(userData , multipartImage)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!! as User
                            Toast.makeText(
                                requireActivity(),
                                "Welcome " + data.name,
                                Toast.LENGTH_SHORT
                            ).show()
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

                            val intent = Intent(requireActivity() , MainActivity::class.java)
                            requireActivity().startActivity(intent)
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "register Request unsuccessfull!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }




            }

        }


    }


    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(requireActivity(), task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent = Intent(requireActivity() , MainActivity::class.java)
                intent.putExtra("email" , account.email)
                intent.putExtra("name" , account.displayName)
                startActivity(intent)
            }else{
                Toast.makeText(requireActivity(), it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }
}


