package repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.request.ProdutoRequest
import model.response.ProdutoResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import repository.service.ProdutoService
import utils.RetrofitInstance
import utils.Utils.BASE_URL
import utils.Utils.Client
import utils.Utils.convert
import java.io.File

class ProdutoRepository private constructor(
    private val service: ProdutoService,
) {
    val produtos = mutableStateMapOf<String, ProdutoResponse>()
    val pageItems = mutableStateListOf<ProdutoResponse>()
    val totalPages = mutableStateOf(-1)

    init {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            pagination()
        }
    }

    suspend fun pagination(pageIndex: Int = 0) = withContext(Dispatchers.IO) {
        val response = service.page(page = pageIndex)
        if (response.isSuccessful) {
            val value = response.body()!!
            totalPages.value = value.totalPages
            pageItems.clear();
            pageItems.addAll(value.data)
        }
    }


    suspend fun getAll() = withContext(Dispatchers.IO) {
        val response = service.findAll()
        if (response.isSuccessful) {
            val data = response.body()
            data?.forEach {
                produtos["${it.produto.id}"] = it
            }
        } else {
            println(response.message())
        }
    }

    suspend fun save(files: List<File>, produto: ProdutoRequest) = withContext(Dispatchers.IO) {
        val data = Gson().toJson(produto)
        val form = MultipartBody.Builder().setType(MultipartBody.FORM)
        files.forEach {
            addFile(file = it, form = form)
        }
        form.addFormDataPart("body", data)
        val request = Request.Builder()
            .url("${BASE_URL}food/produto")
            .method("POST", form.build())
            .build()

        val response = Client.newCall(request).execute()
        if (response.isSuccessful) {
            val json = response.body()!!.string()
            val result = convert<ProdutoResponse?>(ProdutoResponse::class, json)
            println(result!!)
            produtos["${result.produto.id}"] = result
            return@withContext true
        } else {
            println(response.message())
        }
        false
    }

    private fun addFile(file: File, form: MultipartBody.Builder) {
        form.addFormDataPart(
            "file", file.name,
            RequestBody.create(
                MediaType.parse("application/octet-stream"), file
            )
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: ProdutoRepository? = null

        @Synchronized
        fun getInstance(): ProdutoRepository? {
            if (INSTANCE == null) {
                INSTANCE = ProdutoRepository(
                    service = RetrofitInstance.getInstance()!!.create(ProdutoService::class.java),
                )
                return INSTANCE
            }
            return INSTANCE
        }

    }

}
