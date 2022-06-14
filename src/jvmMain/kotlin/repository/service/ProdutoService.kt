package repository.service

import model.response.ProdutoResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProdutoService {

    @GET("food/produto/all")
    suspend fun findAll(): Response<List<ProdutoResponse>>
}