package views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.ScrollableColumn
import model.Usuario
import repository.UserRepository


@Composable
fun Users() {
    val repository = UserRepository.getINSTANCE()!!

    LaunchedEffect(true) {
        repository.users()
    }

    ScrollableColumn {
        repository.users.forEach {
            SampleCard(it.value)
        }
    }
}


@Composable
fun SampleCard(user: Usuario) {
    Surface(
        elevation = 2.dp,
        color = MaterialTheme.colors.primary,
        modifier = Modifier.height(56.dp), shape = RoundedCornerShape(8)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp)
        ) {
            Text(
                text = user.username,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = user.email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )

            Text(
                text = user.isEnable.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1
            )

        }
    }
}