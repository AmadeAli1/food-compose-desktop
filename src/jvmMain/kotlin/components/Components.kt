package components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
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
    validation: String?,
) {
    Column(modifier = Modifier) {
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
        if (validation!!.isNotBlank()) {
            Text(
                text = validation,
                maxLines = 1,
                color = Color.Red,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.End).padding(top = 2.dp)
            )
        }
    }
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

@Composable
fun ScrollableColumn(content: @Composable () -> Unit) {
    val scrollState = rememberScrollState(0)
    val adapter = rememberScrollbarAdapter(scrollState)
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.verticalScroll(scrollState).padding(bottom = 16.dp)
        ) {
            content()
        }
        VerticalScrollbar(adapter = adapter, modifier = Modifier.fillMaxHeight().align(Alignment.TopEnd))
    }
}


@Composable
fun ScrollableLazylist(content: @Composable (LazyListState) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        val state = rememberLazyListState()
        content(state)
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }

}