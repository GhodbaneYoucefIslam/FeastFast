package com.example.feastfast.ui.account



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feastfast.databinding.FragmentFavoriteBinding
import com.example.feastfast.models.retrofit.Endpoint
import kotlinx.coroutines.*

class favoriteFragment : Fragment() {




        lateinit var binding : FragmentFavoriteBinding
        lateinit var myContext : FragmentActivity
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentFavoriteBinding.inflate(inflater,container,false)
            val view = binding.root
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            myContext= requireActivity()
            binding!!.recycleView.layoutManager = LinearLayoutManager(myContext)
            loadData()
        }


        fun loadData(){
            val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
                myContext.runOnUiThread {
                    Toast.makeText(myContext, "request successful with Some unspecified error", Toast.LENGTH_SHORT).show()
                }
            }

            CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
                val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val idUser = pref.getInt("idUser",0)
                val response = Endpoint.createEndpoint().getFavRestaurants(idUser)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!.toList()
                        binding.recycleView.adapter= RestaurantAdapter(data,myContext)
                    } else {
                        Toast.makeText(myContext, "Request unsuccessful!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }