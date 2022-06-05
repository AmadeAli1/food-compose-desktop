package model

import com.google.gson.annotations.SerializedName

class Usuario(username: String, email: String, senha: String) {
    @SerializedName("uid")
    private var uid: String? = null

    @SerializedName("email")
    private var email: String? = null

    @SerializedName("username")
    private var username: String? = null

    private var senha: String? = null

    fun User() {}

    fun User(email: String?, username: String?, senha: String) {
        this.email = email
        this.username = username
        this.senha = senha
    }

    fun setUid(uid: String?) {
        this.uid = uid
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getUid(): String? {
        return uid
    }

    fun getEmail(): String? {
        return email
    }

    fun getUsername(): String? {
        return username
    }

    override fun toString(): String {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}'
    }
}