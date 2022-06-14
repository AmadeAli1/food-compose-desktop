package model.request

data class ProdutoRequest(
    val name: String,
    val category: Int,
    val price: Float,
    val quantity: Int,
    val description: String,
)