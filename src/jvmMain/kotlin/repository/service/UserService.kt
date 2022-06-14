package repository.service

import model.UserRegisterForm
import model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @Headers("Accept: application/json")
    @GET("user/login")
    suspend fun login(@Query("email") email: String, @Query("senha") senha: String): Response<Usuario>

    //@Headers("Accept: application/json")
    @GET("user/all")
    suspend fun allUsers(): Response<List<Usuario>>

    @Headers("Accept: application/json")
    @POST("user/register")
    suspend fun signup(@Body user: UserRegisterForm): Response<Usuario>


}