package model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("uid")
    private var uid: String? = null,
    @SerializedName("email")
    private var email: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("isEnable")
    val isEnable: Boolean? = null,
) {
    constructor() : this("", "", "", null)
}
