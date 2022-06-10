package repository

import model.UserRegisterForm
import model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("login")
    suspend fun login(@Query("email") email: String, @Query("senha") senha: String): Response<Usuario>

    @Headers("Accept: application/json")
    @POST("register")
    suspend fun signup(@Body user: UserRegisterForm): Response<Usuario>

}