package views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.UserRepository
import java.awt.Cursor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterView(repository: UserRepository = UserRepository()) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.LightGray
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

            TextField(
                value = username.value,
                onValueChange = {
                    username.value = it
                },
                shape = RoundedCornerShape(25),
                modifier = Modifier.widthIn(min = 320.dp).height(50.dp),
                placeholder = {
                    Text(
                        "Username", textAlign = TextAlign.Start
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            TextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                shape = RoundedCornerShape(25),
                modifier = Modifier.widthIn(min = 320.dp).height(50.dp),
                placeholder = {
                    Text(
                        "Email", textAlign = TextAlign.Start
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            TextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                shape = RoundedCornerShape(25),
                modifier = Modifier.widthIn(min = 320.dp).height(50.dp),
                placeholder = {
                    Text(
                        "Password", textAlign = TextAlign.Start
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Button(
                onClick = {
                    scope.launch(Dispatchers.Main) {
                        repository.register(email = email.value, username = username.value, senha = username.value)
                    }
                },
                modifier = Modifier.pointerHoverIcon(icon = PointerIcon(cursor = Cursor(Cursor.HAND_CURSOR)))
                    .widthIn(min = 320.dp),
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