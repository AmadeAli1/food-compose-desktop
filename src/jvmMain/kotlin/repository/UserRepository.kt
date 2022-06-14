package repository

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import model.UserRegisterForm
import model.Usuario
import model.response.Categoria
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import repository.service.UserService
import retrofit2.HttpException
import utils.RetrofitInstance
import utils.Utils
import java.io.File


class UserRepository private constructor(
    private val service: UserService,
) {
    val currentUser = mutableStateOf(Usuario())
    val loginState = mutableStateOf(false)
    val validateForm = mutableStateMapOf<String, String>()

    val users = mutableStateMapOf<String, Usuario>()

    init {
        initValidate()
    }

    private fun initValidate() {
        validateForm["email"] = ""
        validateForm["name"] = ""
        validateForm["senha"] = ""
    }

    suspend fun users() = withContext(Dispatchers.IO) {
        val response = service.allUsers()
        if (response.isSuccessful) {
            val body = response.body()!!
            if (body.size != users.size) {
                body.asFlow().collect {
                    users[it.uid] = it
                }
            }
        } else {
            println(response.message())
        }
    }

    suspend fun saveProfile(id: String, file: File) = withContext(Dispatchers.IO) {
        val form = MultipartBody.Builder().setType(MultipartBody.FORM)
        form.addFormDataPart(
            "file", file.name,
            RequestBody.create(
                MediaType.parse("application/octet-stream"), file
            )
        )
        val request = Request.Builder()
            .url("${Utils.BASE_URL}user/profile?userId=$id")
            .method("POST", form.build())
            .build()

        val response = Utils.Client.newCall(request).execute()
        if (response.isSuccessful) {
            val data = Utils.convert<Usuario>(Usuario::class, response.body()!!.string())
            currentUser.value = data
            true
        } else {
            println(response)
            println(response.message())
            false
        }
    }
    suspend fun login(email: String, senha: String): Boolean {
        loginState.value = true
        return withContext(Dispatchers.IO) {
            try {
                val response = service.login(email = email, senha = senha)
                try {
                    if (response.isSuccessful) {
                        loginState.value = false
                        currentUser.value = response.body()!!
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
                    val body = response.body()
                    currentUser.value = body!!
                    println(body.toString())
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
                INSTANCE = UserRepository(
                    service = RetrofitInstance.getInstance()!!
                        .create(UserService::class.java)
                )
                return INSTANCE
            }
            return INSTANCE
        }
    }

}

