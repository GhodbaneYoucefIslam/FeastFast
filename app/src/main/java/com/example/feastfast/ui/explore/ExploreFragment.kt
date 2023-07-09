package com.example.feastfast.ui.explore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feastfast.databinding.FragmentExploreBinding
import com.example.feastfast.models.Restaurant
import com.example.feastfast.models.retrofit.Endpoint
import kotlinx.coroutines.*

class ExploreFragment : Fragment() {

    lateinit var binding : FragmentExploreBinding

    lateinit var myContext : Context
    lateinit var adapter: RestaurantAdapter
    private lateinit var myData : List<Restaurant>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myContext= requireActivity()
        binding.recycleView.layoutManager = LinearLayoutManager(myContext)
        loadData()




        //search logic
        val searchView = binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String?): Boolean {
                search(queryText)
                return false
            }
            override fun onQueryTextChange(queryText: String?): Boolean {
                return false
            }
        })
}


    private fun loadData(){
        val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
            requireActivity().runOnUiThread {
                Toast.makeText(myContext, "request successful with Some unspecified error", Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val pref = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val idUser = pref.getInt("idUser",0)
            val response = Endpoint.createEndpoint().getAllRestaurants(idUser)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.toList()
                    myData=data
                    binding.recycleView.adapter= RestaurantAdapter(data,myContext)
                    adapter = binding.recycleView.adapter as RestaurantAdapter
                } else {
                    Toast.makeText(myContext, "Request unsuccessful!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun search(queryText:String?){
        if (queryText != null && queryText.isNotEmpty()){
            val filteredList = mutableListOf<Restaurant>()
            for(restaurant in myData){
                if (restaurant.name.lowercase().contains(queryText.lowercase())){
                    filteredList.add(restaurant)
                }
            }

            if (filteredList.isNotEmpty()){
                adapter.setFilteredList(filteredList)
            }else{
                Toast.makeText(context,"No matches found" , Toast.LENGTH_SHORT).show()
                adapter.setFilteredList(myData)
            }
        }
    }

}
