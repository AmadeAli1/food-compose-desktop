package repository

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.UserRegisterForm
import retrofit2.HttpException
import utils.RetrofitInstance


class UserRepository private constructor() {
    private val retrofit = RetrofitInstance.getINSTANCE()!!
    private val service = retrofit.create(UserService::class.java)
    val loginState = mutableStateOf(false)
    val register = mutableStateOf("")
    val validateForm = mutableStateMapOf<String, String>()

    init {
        initValidate()
    }

    private fun initValidate() {
        validateForm["email"] = ""
        validateForm["name"] = ""
        validateForm["senha"] = ""

    }

    suspend fun login(email: String, senha: String): Boolean {
        loginState.value = true
        return withContext(Dispatchers.IO) {
            try {
                val response = service.login(email = email, senha = senha)
                try {
                    if (response.isSuccessful) {
                        loginState.value = false
                        println(response.body()!!)
                        return@withContext true
                    } else {
                        println("--------Erro-----------")
                        println(response.errorBody()!!.string())
                        println(response.message())
                        loginState.value = false

                    }
                } catch (e: Exception) {
                    println(e.message)
                }


            } catch (e: HttpException) {
                loginState.value = false
                e.printStackTrace()
            }
            false
        }

    }

    suspend fun signup(email: String, username: String, senha: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                loginState.value = true
                val user = UserRegisterForm(name = username, senha = senha, email = email)
                val response = service.signup(user = user)

                if (response.isSuccessful) {
                    loginState.value = false
                    initValidate()
                    println(response.body()!!.toString())
                    return@withContext true
                } else {
                    loginState.value = false
                    val message = response.errorBody()!!.string()
                    try {
                        val js = JsonParser().parse(message)
                        initValidate()
                        val xs = mutableMapOf<String, String>()
                        js.asJsonArray.forEach {
                            val field = it.asJsonObject.get("field")
                            val msg = it.asJsonObject.get("message")
                            xs[field.asString] = msg.asString
                        }
                        validateForm.putAll(xs)
                    } catch (e: Exception) {
                        println("Json Parser Error: ")
                        println(e.message)
                    }

                }
            } catch (e: Exception) {
                loginState.value = false
                println("Erro: ${e.message}")
            }
            return@withContext false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        @Synchronized
        fun getINSTANCE(): UserRepository? {
            if (INSTANCE == null) {
                INSTANCE = UserRepository()
                return INSTANCE
            }
            return INSTANCE
        }
    }

}

