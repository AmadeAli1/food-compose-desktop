import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.*
import views.HomeView
import views.LoginView
import views.RegisterView
import views.Screen

fun main() = application {

    val state = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        placement = WindowPlacement.Maximized
    )

    val page = remember { mutableStateOf(Screen.Login) }

    Window(
        title = "Food Market",
        icon = rememberVectorPainter(image = Icons.Default.AccountCircle),
        state = state,
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme {
            when (page.value) {
                Screen.Home -> HomeView()
                Screen.Login -> LoginView{
                    page.value = it
                }
                Screen.Register -> RegisterView {
                    page.value = it
                }
            }
        }
    }

}
