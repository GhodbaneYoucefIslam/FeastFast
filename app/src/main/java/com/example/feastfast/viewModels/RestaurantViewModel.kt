package com.example.feastfast.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feastfast.models.FavClickResponse
import com.example.feastfast.models.MenuItem
import com.example.feastfast.models.retrofit.Endpoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantViewModel : ViewModel() {
    private val _data = MutableLiveData<List<MenuItem>>()
    private var _favrite = MutableLiveData<Int>()
    val data: LiveData<List<MenuItem>> get() = _data
    val favrite: LiveData<Int> get() = _favrite

    fun loadData(restaurantId : Int) {
        val exceptionHandler = CoroutineExceptionHandler() { coroutineContext, throwable ->
            val errorMessage = "Error occurred: ${throwable.localizedMessage}"
           // Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = Endpoint.createEndpoint().getRestaurantById(restaurantId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    _data.value = response.body()!!.toList()
                } else {
                    // Handle unsuccessful response
                }
            }
        }
    }

    fun initilizeFavState(state:Int){
        _favrite.value = state

        println(_favrite.value)
    }
    fun handleFavClick(idUser :Int ,idRes:Int?) {
        val exceptionHandler = CoroutineExceptionHandler() { coroutineContext, throwable ->
            val errorMessage = "Error occurred: ${throwable.localizedMessage}"
            // Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show(
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = Endpoint.createEndpoint().favoriteRestaurant(idUser,idRes)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val favClickResponse = response.body()!! as FavClickResponse

                    if (favClickResponse.message == "Restaurant liked successfully" ){
                        _favrite.value = 1
                    }
                    else { _favrite.value = 0 }
                } else {
                    // Handle unsuccessful response
                }
            }
        }
    }
}
