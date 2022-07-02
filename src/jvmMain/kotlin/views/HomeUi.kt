package views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import components.loadNetworkImage
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import repository.UserRepository
import utils.Utils
import views.ui.ProdutoContainer
import java.awt.Cursor

@Composable
fun Demo(state: WindowState) {
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
                        ScreenNavRail.PRODUCTS -> ProdutoContainer()
                        ScreenNavRail.CATEGORIES -> CategoryView()
                    }
                    if (showImage) {
                        Surface(
                            elevation = 5.dp,
                            shape = CircleShape,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            ProfileImageChooser {
                                scope.launch {
                                    val file = Utils.chooseImage()
                                    if (file != null) {
                                        repository.saveProfile(
                                            id = repository.currentUser.value.uid,
                                            file = file
                                        )
                                    }
                                }
                                showImage = false
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun ProfileImageChooser(onClick: () -> Unit) {
    Surface(color = Color(0xFFcb9b8c)) {
        Image(
            imageVector = Icons.Filled.PhotoCamera,
            contentDescription = null,
            modifier = Modifier.padding(16.dp)
                .size(150.dp)
                .clip(CircleShape)
                .clickable {
                    onClick()
                }
        )
    }
}

@Composable
fun Main() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {}
}


enum class ScreenNavRail(val title: String) {
    USERS("Users"),
    PRODUCTS("Products"),
    CATEGORIES("Categories"),
    MAIN("Home")
}

data class NavItems(
    val icon: ImageVector,
    val screenNavRail: ScreenNavRail,
)

private fun items() = listOf(
    NavItems(Icons.Outlined.Home, ScreenNavRail.MAIN),
    NavItems(Icons.Outlined.ShoppingCart, ScreenNavRail.PRODUCTS),
    NavItems(Icons.Outlined.PersonOutline, ScreenNavRail.USERS),
    NavItems(Icons.Outlined.Category, ScreenNavRail.CATEGORIES)
)

@Composable
private fun NavRailItems(
    selected: MutableState<Int>,
    onNavClick: (ScreenNavRail) -> Unit,
) {
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
            label = {
                Text(text = item.screenNavRail.title)
            },
            alwaysShowLabel = true,
            selectedContentColor = Color.White
        )
    }
}

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



















