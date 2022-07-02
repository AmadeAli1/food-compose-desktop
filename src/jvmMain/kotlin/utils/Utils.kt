package utils

import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.awt.Container
import java.io.File
import java.time.Duration
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.reflect.KClass

object Utils {
    const val BASE_URL = "https://food-api-amade.herokuapp.com/api/"

    @JvmStatic
    val Client: OkHttpClient = OkHttpClient().newBuilder()
        .callTimeout(Duration.ofMinutes(3L))
        .connectTimeout(Duration.ofMinutes(3L))
        .readTimeout(Duration.ofMinutes(3L))
        .build()


    fun chooseImage(): File? {
        val chooser = JFileChooser()
        chooser.dialogTitle = "Imagem Para Categoria"
        val filter = FileNameExtensionFilter(
            "JPG & PNG Images", "jpg", "png"
        )
        chooser.fileFilter = filter
        chooser.isMultiSelectionEnabled = false
        val returnVal = chooser.showOpenDialog(Container())
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            println(
                "Accept"
            )
            return chooser.selectedFile
        }
        return null
    }


    fun chooseMultipleImages(): List<File>? {
        val chooser = JFileChooser()
        chooser.dialogTitle = "Imagem Para Categoria"
        val filter = FileNameExtensionFilter(
            "JPG & PNG Images", "jpg", "png"
        )
        chooser.fileFilter = filter
        chooser.isMultiSelectionEnabled = true
        val returnVal = chooser.showOpenDialog(Container())
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            println(
                "Accept"
            )
            if (chooser.selectedFiles.isEmpty()) {
                return null
            }
            return chooser.selectedFiles.toList()
        }
        return null
    }


    fun <T> convert(clazz: KClass<*>, response: String): T {
        return Gson().fromJson<T>(
            response, clazz.java
        )
    }
}