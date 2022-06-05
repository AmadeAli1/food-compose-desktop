package model

import com.google.gson.annotations.SerializedName

data class UserRegisterForm(
    @SerializedName("name") var name: String,
    @SerializedName("Senha") var senha: String,
    @SerializedName("email") var email: String,
) : java.io.Serializable {
}