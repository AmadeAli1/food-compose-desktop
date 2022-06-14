package model.response

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Produto(
    val name: String,
    val price: Double,
    val quantity: Int,
    val category: Int,
    val likes: Int,
    val description: String,
    val id: Int,
    private val createdAt: String,
) {

    constructor() : this("", 0.0, 0, 0, 0, "", -1, "")

    fun date(): LocalDateTime {
        return LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME)
    }

    override fun toString(): String {
        return "Produto(category=$category, id=$id, likes=$likes, name='$name', price=$price, " +
                "quantity=$quantity, date=${date()},description=$description)"
    }

}