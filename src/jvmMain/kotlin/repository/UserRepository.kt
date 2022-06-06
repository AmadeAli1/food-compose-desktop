package repository

import androidx.compose.runtime.mutableStateOf
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.UserRegisterForm
import model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import utils.ErrorBody
import utils.RetrofitInstance


class UserRepository {
    private val retrofit = RetrofitInstance.getINSTANCE()!!
    private val service = retrofit.create(UserService::class.java)
    val loginState = mutableStateOf(false)
    val register = mutableStateOf("")

    fun login(email: String, senha: String) {
        loginState.value = true

        try {
            service.login(email = email, senha = senha).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    try {
                        if (response.isSuccessful) {
                            loginState.value = false
                            println(response.body()!!)
                        } else {
                            println("--------Erro-----------")
                            println(response.errorBody()!!.string())
                            println(response.message())
                            loginState.value = false
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    loginState.value = false
                    t.printStackTrace()
                }
            })
        } catch (e: HttpException) {
            loginState.value = false
            e.printStackTrace()
        }

    }

    suspend fun signup(email: String, username: String, senha: String) {
        withContext(Dispatchers.IO) {
            try {
                loginState.value = true
                val user = UserRegisterForm(name = username, senha = senha, email = email)
                val response = service.signup(user = user)
                if (response.isSuccessful) {
                    loginState.value = false
                    println(response.body()!!.toString())
                } else {
                    loginState.value = false
                    val message = response.errorBody()!!.string()
                    val js = JsonParser().parse(message)

                    val erros = js.asJsonArray.map {
                        val field = it.asJsonObject.get("field")
                        val message = it.asJsonObject.get("message")
                        ErrorBody(field.asString, message.asString)
                    }

                    println(erros)

                }
            } catch (e: HttpException) {
                loginState.value = false
                println("Erro: ${e.message}")
            }
        }
    }

    fun register(email: String, username: String, senha: String) {
        loginState.value = true
        val user = UserRegisterForm(name = username, senha = senha, email = email)
        println(user)
        try {
            service.register(user).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        loginState.value = false
                        val us = response.body()!!
                        register.value = "Welcome ${us.getUsername()}"
                    } else {
                        loginState.value = false
                        println(response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        } catch (e: HttpException) {
            e.printStackTrace()
            loginState.value = false
        }
    }

}