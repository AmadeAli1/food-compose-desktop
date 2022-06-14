package model.response

data class ProdutoResponse(
    val categoria: Categoria,
    val images: Collection<String>,
    val produto: Produto,
)