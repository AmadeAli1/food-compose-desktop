import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import views.RegisterView

fun main() = application {

    val state = rememberWindowState(
        position = WindowPosition(Alignment.Center)
    )

    Window(
        title="Food Market",
        //state = state,
        onCloseRequest = ::exitApplication
    ) {
        //LoginView()
        RegisterView()
    }


}
