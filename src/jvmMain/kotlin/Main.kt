import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import views.LoginView
import views.RegisterView

fun main() = application {

    Window(onCloseRequest = ::exitApplication) {
        LoginView()
        //RegisterView()
    }

}
