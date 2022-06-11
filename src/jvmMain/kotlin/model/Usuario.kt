package model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("isEnable")
    val isEnable: Boolean,
    @SerializedName("imageUrl")
    val profileUrl: String? = null,
) {
    constructor() : this("", "", "", false,null)
}
