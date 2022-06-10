package views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import components.ButtonForm
import components.EditText
import kotlinx.coroutines.launch
import repository.UserRepository

@Composable
fun RegisterView(onChangeScreen: (Screen) -> Unit) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val repository = remember { mutableStateOf(UserRepository.getINSTANCE()!!) }.value
    val validation = repository.validateForm
    val scope = rememberCoroutineScope()

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
                    modifier = Modifier.width(320.dp).height(450.dp),
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
                        value = email,
                        label = "Email",
                        icon = Icons.Outlined.Email,
                        onTextChange = {},
                        validation = validation["email"]
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))

                    EditText(
                        modifier = Modifier.widthIn(min = 320.dp).height(50.dp),
                        value = username,
                        label = "Username",
                        icon = Icons.Outlined.Person,
                        onTextChange = {},
                        validation = validation["name"]
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    EditText(
                        modifier = Modifier.widthIn(min = 320.dp).height(50.dp),
                        value = password,
                        label = "Password",
                        icon = Icons.Outlined.Lock,
                        onTextChange = {},
                        visualTransformation = PasswordVisualTransformation(),
                        validation = validation["senha"],
                    )
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    ButtonForm("Register", onClick = {
                        scope.launch {
                            val signup = repository.signup(
                                email = email.value,
                                senha = password.value,
                                username = username.value
                            )
                            if (signup) {
                                onChangeScreen(Screen.Home)
                            }
                        }
                    })
                    AnimatedVisibility(visible = repository.loginState.value) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

    }


}
