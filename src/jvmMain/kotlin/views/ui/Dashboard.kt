package views.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.websocket.*


@Composable
fun Dashboard() {

}



@Composable
private fun Item(title: String, image: ImageBitmap, quantity: Int) {
    Card(
        elevation = 3.dp,
        shape = RoundedCornerShape(8),
        modifier = Modifier.width(150.dp).height(70.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop, modifier = Modifier.size(60.dp).clip(CircleShape)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = quantity.toString(), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                Text(text = title)
            }
        }
    }
}