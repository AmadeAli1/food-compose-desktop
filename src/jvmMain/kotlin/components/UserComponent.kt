package components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Usuario
import org.jetbrains.skia.Image
import views.loadPicture
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UserCard(modifier: Modifier = Modifier, usuario: Usuario) {
    val scope = rememberCoroutineScope()
    val image = remember { mutableStateOf(ImageBitmap(1, 1)) }
    val loadImage = scope.async {
        mutableStateOf(loadPicture(usuario.profileUrl!!))
    }
    scope.launch {
        image.value = loadImage.await().value
    }
    Card(
        modifier = modifier,
        elevation = 5.dp,
        backgroundColor = Color.LightGray,
        shape = RoundedCornerShape(6)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
        ) {
            AnimatedVisibility(
                image.value.width == 1,
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier =
                    Modifier.padding(8.dp).size(160.dp)
                        .clip(CircleShape)
                )
            }
            AnimatedVisibility(
                visible = image.value.width != 1,
                enter = expandVertically()
            ) {
                Image(
                    bitmap = image.value, contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Text(text = usuario.username!!)
            Text(text = usuario.email!!)
        }
    }
}

suspend fun loadNetworkImage(link: String): ImageBitmap = withContext(Dispatchers.IO) {
    val url = URL(link)
    val connection = url.openConnection() as HttpURLConnection
    connection.connect()
    val inputStream = connection.inputStream
    val bufferedImage = ImageIO.read(inputStream)
    val stream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "png", stream)
    val byteArray = stream.toByteArray()
    Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}