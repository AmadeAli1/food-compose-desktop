package views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import components.ScrollableColumn
import components.loadNetworkImage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import repository.CategoriaRepository
import theme.lightPalete
import utils.Utils
import java.awt.Cursor
import java.io.File

@Composable
fun CategoryView(
    repository: CategoriaRepository = CategoriaRepository.getInstance()!!,
) {

    MaterialTheme(colors = lightPalete) {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize().padding(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Header()
                SaveForm(repository = repository)
                ScrollableColumn {
                    Body(repository)
                }
            }
        }
    }
}

@Composable
fun BodyHeader() {
    Card(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Id", style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1.0f).wrapContentWidth(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Categoria", style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(6.5f).wrapContentWidth(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )

            Image(
                imageVector = Icons.Outlined.Category,
                null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier.clip(CircleShape).size(35.dp)
            )

        }

    }
}

@Composable
fun Body(repository: CategoriaRepository) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        BodyHeader()
        repository.categorias.forEach { (_, value) ->
            BodyItem(
                elevate = 0.dp, id = "${value.id}",
                title = value.name,
                imageUrl = value.image
            )
        }
    }
}

@Composable
fun BodyItem(
    elevate: Dp, id: String, title: String, imageUrl: String,
) {
    val scope = rememberCoroutineScope()
    val image = remember { mutableStateOf(ImageBitmap(0, 0)) }
    val loadImage = scope.async {
        mutableStateOf(loadNetworkImage(imageUrl))
    }
    scope.launch {
        image.value = loadImage.await().value
    }

    Card(
        modifier = Modifier.fillMaxWidth().height(90.dp),
        elevation = elevate,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = id, style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1.0f).wrapContentWidth(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title, style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(6.5f).wrapContentWidth(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold
            )
            AnimatedVisibility(visible = image.value.width != 0) {
                Image(
                    bitmap = image.value, null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape).size(50.dp)
                )
            }
            AnimatedVisibility(visible = image.value.width == 0) {
                Image(
                    imageVector = Icons.Filled.BrokenImage, null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape).size(50.dp)
                )
            }
        }
    }
}


@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CATEGORIAS",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SaveForm(repository: CategoriaRepository) {
    Surface(
        elevation = 1.dp, modifier = Modifier,
        color = MaterialTheme.colors.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp).height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val name = remember { mutableStateOf("") }
            val fileUpload = remember { mutableStateOf<File?>(File("")) }
            var progress by remember { mutableStateOf(false) }
            val enableSave = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                placeholder = { Text(text = "Nome da categoria") },
                maxLines = 1,
                trailingIcon = { Icon(imageVector = Icons.Outlined.Category, null) },
                shape = RoundedCornerShape(5),
                modifier = Modifier.weight(3.5f).size(50.dp)
            )

            OutlinedButton(
                onClick = {
                    scope.launch {
                        val file = Utils.chooseImage()
                        if (file == null) {
                            println("File must be not null")
                            enableSave.value = false
                        } else {
                            fileUpload.value = file
                            enableSave.value = true
                        }
                    }
                },
                //colors = ButtonDefaults.outlinedButtonColors(backgroundColor = White),
                modifier = Modifier.weight(3f).height(50.dp)
                    .pointerHoverIcon(icon = PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
            ) {
                Text(text = "Imagem")
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Icon(imageVector = Icons.Outlined.AttachFile, null)
            }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        progress = true
                        if (name.value.isNotBlank()) {
                            if (repository.validate(name.value)) {
                                val status = repository.save(name.value, file = fileUpload.value!!)
                                if (status) {
                                    resetFields(name, fileUpload, enableSave)
                                }
                            } else {
                                println("A categoria ja existe!")
                                resetFields(name, fileUpload, enableSave)
                            }
                        }
                        progress = false
                    }
                },
                enabled = enableSave.value,
                modifier = Modifier.weight(3f).height(50.dp)
                    .pointerHoverIcon(icon = PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
            ) {
                Text(text = "Gravar")
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                if (progress) {
                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                } else {
                    Icon(imageVector = Icons.Outlined.Save, null)
                }
            }
        }
    }

}

private fun resetFields(
    name: MutableState<String>,
    fileUpload: MutableState<File?>,
    enableSave: MutableState<Boolean>,
) {
    name.value = ""
    fileUpload.value = null
    enableSave.value = false
}

suspend fun loadPicture(url: String): ImageBitmap {
    val image = HttpClient(CIO)
    val body = image.use { client ->
        client.get(url)
    }.call.body<ByteArray>()
    println(body)
    return Image.makeFromEncoded(body).toComposeImageBitmap()
}
