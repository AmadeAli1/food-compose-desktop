package views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.UserRepository
import java.awt.Cursor

@Composable
fun LoginView(onSuccessLogin: (Screen) -> Unit) {
    val repository = UserRepository.getINSTANCE()!!

    val username = remember { mutableStateOf("aliamade29@gmail.com") }
    val password = remember { mutableStateOf("amade123") }
    val scope = rememberCoroutineScope()

    scope.launch {
        val login = repository.login(email = username.value, senha = password.value)
        if (login) {
            onSuccessLogin(Screen.Home)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Gray
    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                color = Color.LightGray, elevation = 10.dp, shape = RoundedCornerShape(5),
                modifier = Modifier.size(width = 360.dp, 450.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.DarkGray,
                        modifier = Modifier.size(120.dp).clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.padding(vertical = 6.dp))

                    EditText(
                        modifier = Modifier.widthIn(min = 320.dp).height(50.dp),
                        value = username,
                        label = "Username",
                        icon = Icons.Outlined.Person,
                        onTextChange = {},
                        validation = ""
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))

                    EditText(
                        modifier = Modifier.widthIn(min = 320.dp).height(50.dp),
                        value = password,
                        label = "Password",
                        icon = Icons.Outlined.Lock,
                        onTextChange = {},
                        validation = "",
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Button(
                        onClick = {
                            scope.launch(Dispatchers.Main) {
                                val login = repository.login(email = username.value, senha = password.value)
                                if (login) {
                                    onSuccessLogin(Screen.Home)
                                }
                            }
                        },
                        modifier = Modifier.pointerHoverIcon(
                            icon = PointerIcon(cursor = Cursor(Cursor.HAND_CURSOR))
                        ),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
                    ) {
                        Text(
                            "Login", textAlign = TextAlign.Center, color = Color.White, fontWeight = FontWeight.SemiBold
                        )
                    }

                    AnimatedVisibility(visible = repository.loginState.value) {
                        CircularProgressIndicator()
                    }

                }
            }
        }

    }

}