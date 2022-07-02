package repository

import androidx.compose.runtime.mutableStateMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.response.Categoria
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import repository.service.CategoriaService
import utils.RetrofitInstance
import utils.Utils
import utils.Utils.Client
import utils.Utils.convert
import java.awt.Container
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


class CategoriaRepository private constructor(
    private val service: CategoriaService,
) {
    val categorias = mutableStateMapOf<String, Categoria>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getAll()
        }
    }

    private suspend fun getAll() {
        val response = service.findAll()
        if (response.isSuccessful) {
            val body = response.body()
            body?.forEach {
                categorias["${it.id}"] = it
            }
        }else{
            println(response.message())
        }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val delete = service.delete(id)
        if (delete.isSuccessful) {
            categorias.remove("$id")
        } else {
            println(delete.message())
        }
    }

    fun validate(name: String): Boolean {
        val values = categorias.filterValues { it.name.equals(name, true) }
        return values.isEmpty()
    }


    suspend fun save(name: String, file: File) = withContext(Dispatchers.IO) {

        val form = MultipartBody.Builder().setType(MultipartBody.FORM)
        form.addFormDataPart(
            "file", file.name,
            RequestBody.create(
                MediaType.parse("application/octet-stream"), file
            )
        )
        form.addFormDataPart("name", name)
        val request = Request.Builder()
            .url("${Utils.BASE_URL}food/categoria")
            .method("POST", form.build())
            .build()

        val response = Client.newCall(request).execute()
        if (response.isSuccessful) {
            val data = convert<Categoria>(Categoria::class, response.body()!!.string())
            categorias["${data.id}"] = data
            true
        } else {
            println(response)
            println(response.message())
            false
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: CategoriaRepository? = null

        @Synchronized
        fun getInstance(): CategoriaRepository? {
            if (INSTANCE == null) {
                INSTANCE = CategoriaRepository(
                    service = RetrofitInstance.getInstance()!!.create(CategoriaService::class.java),
                )
                return INSTANCE
            }
            return INSTANCE
        }
    }

}
