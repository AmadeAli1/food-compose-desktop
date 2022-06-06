package repository

import model.UserRegisterForm
import model.Usuario
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @Headers("Accept: application/json")
    @POST("register")
    fun register(@Body user: UserRegisterForm): Call<Usuario>

    @GET("login")
    fun login(@Query("email") email: String, @Query("senha") senha: String): Call<Usuario>

    @Headers("Accept: application/json")
    @POST("register")
    suspend fun signup(@Body user: UserRegisterForm): Response<Usuario>

}