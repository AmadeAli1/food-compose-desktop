package repository.service

import model.response.Page
import model.response.ProdutoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProdutoService {

    @GET("food/produto/all")
    suspend fun findAll(): Response<List<ProdutoResponse>>

    @GET("food/produto")
    suspend fun page(@Query("page") page: Int): Response<Page<ProdutoResponse>>

}