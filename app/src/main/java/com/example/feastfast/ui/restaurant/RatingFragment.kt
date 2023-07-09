package com.example.feastfast.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.feastfast.databinding.FragmentRatingBinding
import com.example.feastfast.models.retrofit.Endpoint
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*


class RatingFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentRatingBinding
    lateinit var myContext : FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRatingBinding.inflate(inflater,container,false)
        val view = binding.root
        val behaviour = BottomSheetBehavior.from(binding.layoutRoot)
        behaviour.isHideable = false
        behaviour.isDraggable=false
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val idRes = bundle?.getInt("id", 0)
        val average = bundle?.getFloat("average", 0.5F)
        val count = bundle?.getInt("count", 0)

        binding.ratingCount3.text = average.toString()
        binding.ratingNumber3.text = "("+count.toString()+")"
        loadRating(idRes,count)
        myContext = requireActivity()




    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disableBottomSheetDraggableBehavior()
    }

    private fun disableBottomSheetDraggableBehavior() {
        this.isCancelable = false
        this.dialog?.setCanceledOnTouchOutside(true)
    }
    private  fun loadRating (idRes:Int?,total:Int?) {

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            requireActivity().runOnUiThread {
                val errorMessage = "Error occurred: ${throwable.localizedMessage}"
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

            CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val response = Endpoint.createEndpoint().getRestauranteRating(idRes)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!.toList()
                        binding.star5Percentage.text = if (total != 0) ((data[4]*100/ total!!)).toString()+"%" else "0%"
                        binding.stars4Percentage.text =  if (total != 0) ((data[3]*100/ total!!)).toString()+"%" else "0%"
                        binding.star3Percentage.text =  if (total != 0) ((data[2]*100/ total!!)).toString()+"%" else "0%"
                        binding.star2Percentage.text =  if (total != 0) ((data[1]*100/ total!!)).toString()+"%" else "0%"
                        binding.star1Percentage.text =  if (total != 0) ((data[0]*100/ total!!)).toString()+"%" else "0%"

                        binding.stars5progressBar.progress  = if (total != 0) ((data[4]*100/ total!!))  else 0
                        binding.stars4progressBar.progress =  if (total != 0) ((data[3]*100/ total!!))  else 0
                        binding.stars3progressBar.progress =  if (total != 0) ((data[2]*100/ total!!))  else 0
                        binding.stars2progressBar.progress =  if (total != 0) ((data[1]*100/ total!!))  else 0
                        binding.stars1progressBar.progress =  if (total != 0) ((data[0]*100/ total!!))  else 0
                        //binding.
                    } else {
                        Toast.makeText(myContext, "Request unsuccessful!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }