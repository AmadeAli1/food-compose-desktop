package views

import androidx.compose.runtime.Composable

enum class Screen(content: @Composable () -> Unit) {
    Login({ LoginView({}) }),
    Register({ RegisterView({}) }),
    Home({ HomeView() })
}