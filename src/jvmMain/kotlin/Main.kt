import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.*
import views.RegisterView

fun main() = application {

    val state = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        placement = WindowPlacement.Maximized
    )

    Window(
        title = "Food Market",
        icon = rememberVectorPainter(image = Icons.Default.AccountCircle),
        state = state,
        onCloseRequest = ::exitApplication
    ) {
        //LoginView()
        RegisterView()
    }


}
