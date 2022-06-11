package views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.UserCard
import components.loadNetworkImage
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import model.Usuario
import repository.UserRepository
import theme.lightPalete
import java.awt.Cursor

@Composable
fun HomeView(repository: UserRepository = UserRepository.getINSTANCE()!!) {
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()
    val scrollState = rememberScrollState(0)
    val scroll = rememberScrollbarAdapter(scrollState)
    val list = mutableStateListOf<Usuario>()

    scope.launch {
        val users = repository.users()
        list.addAll(users!!)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.verticalScroll(state = scrollState)) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        list.forEach {
                            UserCard(modifier = Modifier.width(300.dp).height(300.dp), it)
                        }
                    }
                }
            }

            VerticalScrollbar(adapter = scroll, modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd))
        }

    }

}

@Composable
fun Demo() {
    var navRailVisible by remember { mutableStateOf(false) }
    val navRailSelected = remember { mutableStateOf(0) }
    var currentScreen by remember { mutableStateOf(ScreenNavRail.MAIN) }
    val scaffoldState = rememberScaffoldState()
    val repository = UserRepository.getINSTANCE()!!
    var showImage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val image = remember { mutableStateOf(ImageBitmap(1, 1)) }

    if (repository.currentUser.value.profileUrl != null) {
        val loadImage = scope.async {
            mutableStateOf(loadNetworkImage(repository.currentUser.value.profileUrl!!))
        }
        scope.launch {
            image.value = loadImage.await().value
        }
    }

    MaterialTheme(colors = lightPalete) {
        Scaffold(
            topBar = {
                Toolbar(title = repository.currentUser.value.username, image = image, onNavClick = {
                    navRailVisible = !navRailVisible
                }, onProfileClick = {
                    showImage = !showImage
                })
            },
            scaffoldState = scaffoldState
        ) { paddingValues ->
            Surface(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Row {
                    if (navRailVisible) {
                        NavigationRail(
                            modifier = Modifier.width(120.dp).fillMaxHeight().weight(1f),
                            backgroundColor = MaterialTheme.colors.primary,
                            elevation = 2.dp
                        ) {
                            NavRailItems(selected = navRailSelected) {
                                currentScreen = it
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize().weight(10f)
                    ) {
                        when (currentScreen) {
                            ScreenNavRail.MAIN -> Main()
                            ScreenNavRail.USERS -> Users()
                            ScreenNavRail.PRODUCTS -> Main()
                        }
                        if (showImage) {
                            Surface(
                                elevation = 5.dp,
                                shape=CircleShape,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Image(
                                    bitmap = image.value,
                                    contentDescription = null,
                                    modifier = Modifier.size(150.dp).clickable {
                                        showImage = false
                                    }.clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Main() {
    Box(
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun Users() {
    val users = remember { mutableStateListOf<Usuario>() }
    val repository = UserRepository.getINSTANCE()!!
    LaunchedEffect(true) {
        users.addAll(repository.users()!!)
    }

    ScrollableColumn {
        users.forEach {
            SampleCard(it)
        }
    }

}

enum class ScreenNavRail(val title: String) {
    USERS("Users"),
    PRODUCTS("Products"),
    MAIN("Home")
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

@Composable
private fun ScrollableColumn(content: @Composable () -> Unit) {
    val scrollState = rememberScrollState(0)
    val adapter = rememberScrollbarAdapter(scrollState)
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            content()
        }
        VerticalScrollbar(adapter = adapter, modifier = Modifier.fillMaxHeight().align(Alignment.TopEnd))
    }
}


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

data class NavItems(
    val icon: ImageVector,
    val screenNavRail: ScreenNavRail,
)

private fun items() = listOf(
    NavItems(Icons.Outlined.Home, ScreenNavRail.MAIN),
    NavItems(Icons.Outlined.ShoppingCart, ScreenNavRail.PRODUCTS),
    NavItems(Icons.Outlined.PersonOutline, ScreenNavRail.USERS)
)

@Composable
private fun NavRailItems(selected: MutableState<Int>, onNavClick: (ScreenNavRail) -> Unit) {
    items().forEachIndexed { index, item ->
        NavigationRailItem(
            selected = selected.value == index,
            onClick = {
                selected.value = index
                onNavClick(item.screenNavRail)
            },
            icon = {
                Icon(
                    imageVector = item.icon, contentDescription = null
                )
            },
            label = { Text(text = item.screenNavRail.title) },
            alwaysShowLabel = true,
            selectedContentColor = Color.White
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Toolbar(
    title: String, image: MutableState<ImageBitmap>,
    onNavClick: () -> Unit, onProfileClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 1.dp,
        title = { },
        navigationIcon = {
            IconButton(
                onClick = { onNavClick() },
                modifier = Modifier.pointerHoverIcon(
                    icon = PointerIcon(
                        cursor = Cursor(Cursor.HAND_CURSOR)
                    )
                ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        actions = {
            Text(
                title,
                style = MaterialTheme.typography.subtitle2, fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    onProfileClick()
                },
                modifier = Modifier.pointerHoverIcon(icon = PointerIcon(cursor = Cursor(Cursor.HAND_CURSOR)))
            ) {
                if (image.value.width == 1) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp).clip(CircleShape)
                    )
                } else {
                    Image(
                        bitmap = image.value,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    )
}



















