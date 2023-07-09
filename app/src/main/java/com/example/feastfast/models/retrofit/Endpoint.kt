package com.example.feastfast.models.retrofit
import com.example.feastfast.models.*
import com.example.feastfast.util.url
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


const val url = "https://1377-41-220-149-75.ngrok-free.app/"


interface Endpoint {
    @GET("res/{idUser}")
    suspend fun getAllRestaurants(@Path("idUser") idUser: Int) : Response<List<Restaurant>>

    @GET("res/topRaters/{idUser}")
    suspend fun getAllRestaurantsTopRaters(@Path("idUser") idUser: Int) : Response<List<Restaurant>>
    @GET("res/fav/{idUser}")
    suspend fun  getFavRestaurants(@Path("idUser") idUser: Int) : Response<List<Restaurant>>

    @GET("res/{id}/menu")
    suspend fun getRestaurantById(@Path("id") id: Int) : Response<List<MenuItem>>

    @GET("res/resbyid/{idRes}/{idUser}")
    suspend fun getUniqueRestaurantById(@Path("idRes") idRes: Int, @Path("idUser") idUser: Int) : Response<Restaurant>

    @POST("orders/creatOrder")
    suspend fun sendOrder(
        @Body order: Order
    ): Response<Order>

    @POST("res/rating")
    suspend fun sendRating(
        @Body rating: Rating
    ): Response<Rating>


    @GET("orders/getorders/{userId}")
    suspend fun getUserOrders(
        @Path("userId") userId : Int
    ): Response<List<Order>>

    @GET("res/ratings/{idRes}")
    suspend fun getRestauranteRating(@Path("idRes") idRes: Int?) : Response<List<Int>>




    @GET("users/{idUser}/{idRes}")
    suspend fun favoriteRestaurant(@Path("idUser") idUser: Int , @Path("idRes") idRes: Int?  ) : Response<FavClickResponse>


    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<User>

    @Multipart
    @POST("users/edit")
    suspend fun editProfile(

        @Part userData: MultipartBody.Part,
        @Part file: MultipartBody.Part
    ):Response<User>
    @Multipart
    @POST("users/register")
    suspend fun register(

        @Part userData: MultipartBody.Part,
        @Part file: MultipartBody.Part


    ):Response<User>

    companion object {
        var endpoint: Endpoint? = null
        fun createEndpoint(): Endpoint {
            if(endpoint ==null) {
                endpoint = Retrofit.Builder().baseUrl(url). addConverterFactory(GsonConverterFactory.create()).build(). create(Endpoint::class.java)
            }
            return endpoint!!
        }
    }

}