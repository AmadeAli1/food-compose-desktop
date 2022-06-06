package components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.awt.Cursor

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    value: MutableState<String>,
    label: String,
    icon: ImageVector,
    onTextChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value.value,
        onValueChange = {
            value.value = it
            onTextChange(it)
        },
        shape = RoundedCornerShape(25),
        modifier = modifier,
        placeholder = {
            Text(
                label, textAlign = TextAlign.Start
            )
        },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        maxLines = 1
    )
}

@Composable
fun ButtonForm(
    text: String,
    color: Color = Color.DarkGray,
    onClick: () -> Unit,
) {

    Button(
        onClick = { onClick() },
        modifier = Modifier.pointerHoverIcon(icon = PointerIcon(cursor = Cursor(Cursor.HAND_CURSOR)))
            .widthIn(min = 320.dp),
        shape = RoundedCornerShape(25),
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Text(
            text, textAlign = TextAlign.Center, color = Color.White, fontWeight = FontWeight.SemiBold
        )
    }
}
