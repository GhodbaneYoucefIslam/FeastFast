package com.example.feastfast.ui.orders

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.feastfast.R
import com.example.feastfast.databinding.FragmentRatingPopUpBinding
import com.example.feastfast.models.Rating
import com.example.feastfast.models.retrofit.Endpoint
import kotlinx.coroutines.*


class RatingPopUp : DialogFragment() {

    lateinit var binding: FragmentRatingPopUpBinding
    lateinit var myContext: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatingPopUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myContext= requireActivity()
        binding.buttonSubmit.backgroundTintList =  ColorStateList.valueOf( ContextCompat.getColor(myContext,R.color.primary_color)  )
        binding.buttonSubmit.setOnClickListener {
            val review = binding.editText.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            val bundle = arguments
            val userId = bundle!!.getInt("userId")
            val restaurantId = bundle!!.getInt("restaurantId")

            val ratingObject = Rating(restaurantId,userId,rating,review)

            val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
                myContext.runOnUiThread {
                    Toast.makeText(myContext, "request successful with Some unspecified error", Toast.LENGTH_SHORT).show()
                }
            }

            CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response = Endpoint.createEndpoint().sendRating(ratingObject)
                withContext(Dispatchers.Main){
                    if (response.isSuccessful && response.body()!=null){
                        val ratingThatWasSent = response.body() as Rating
                        Toast.makeText(myContext, " Your review ${ratingThatWasSent.review} has been sent successfully ", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }else{
                        Toast.makeText(myContext, " The request was launched but unsuccessful ", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

    }

}