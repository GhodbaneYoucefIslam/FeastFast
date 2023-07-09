package com.example.feastfast.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feastfast.databinding.FragmentOrdersBinding
import com.example.feastfast.models.Order
import com.example.feastfast.models.retrofit.Endpoint
import kotlinx.coroutines.*

class OrdersFragment : Fragment() {
    lateinit var binding : FragmentOrdersBinding
    lateinit var myContext : Context
    lateinit var adapter: OrdersAdapter
    lateinit var data : List<Order>
    private lateinit var myActivity : AppCompatActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myContext= requireActivity()
        myActivity = activity as AppCompatActivity
        binding.recycleView.layoutManager = LinearLayoutManager(myContext)
        loadData()

    }

    private fun loadData(){
        val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
            requireActivity().runOnUiThread {
                val errorMessage = "Error occurred: ${throwable.localizedMessage}"
                Toast.makeText(myContext, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val idUser = pref.getInt("idUser",0)
            val response = Endpoint.createEndpoint().getUserOrders(idUser)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.toList()
                    binding.recycleView.adapter = OrdersAdapter(data,myContext,myActivity)
                } else {
                    Toast.makeText(myContext, "Request unsuccessful!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}