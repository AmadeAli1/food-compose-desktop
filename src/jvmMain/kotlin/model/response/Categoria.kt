package model.response

import androidx.compose.ui.graphics.ImageBitmap

data class Categoria(
    val id: Int,
    val image: String,
    val name: String,
) {
    var imageBitmap: ImageBitmap? = null
    fun setImage(image: ImageBitmap): ImageBitmap {
        imageBitmap = image
        return image
    }

}