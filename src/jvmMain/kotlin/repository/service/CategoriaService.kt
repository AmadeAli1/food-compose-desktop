package repository.service

import model.response.Categoria
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoriaService{

    @GET("food/categoria")
    suspend fun findAll(): Response<List<Categoria>>

    @DELETE("food/categoria")
    suspend fun delete(@Query("id") id: Int): Response<String>
}