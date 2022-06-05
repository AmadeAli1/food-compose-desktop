import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import views.LoginView

fun main() = application {


    Window(onCloseRequest = ::exitApplication) {
        LoginView()
    }

}
