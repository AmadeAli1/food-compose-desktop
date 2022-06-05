package repository

import androidx.compose.runtime.mutableStateOf
import model.UserRegisterForm
import model.Usuario
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class UserRepository {
    private val retrofit = Retrofit.Builder().baseUrl("https://food-api-amade.herokuapp.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val userService = retrofit.create(UserService::class.java)
    private val service = retrofit.create(UserService::class.java)

    val loginState = mutableStateOf(false)

    fun login(email: String, senha: String) {
        loginState.value = true

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
            }
        })

    }

    fun register(email: String, username: String, senha: String) {
        loginState.value = true
        val user = UserRegisterForm(name = username, senha = senha, email = email)
        try {
            service.register(user).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        loginState.value = false
                        println(response.body()!!)
                        println("REGISTER")
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