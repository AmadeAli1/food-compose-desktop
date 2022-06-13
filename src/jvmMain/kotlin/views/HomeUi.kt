package views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BabyChangingStation
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.UserCard
import components.loadNetworkImage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import model.Usuario
import org.jetbrains.skiko.redrawer.MainUIDispatcher
import repository.UserRepository
import theme.lightPalete
import java.awt.Cursor
import java.io.File

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
                            ScreenNavRail.PRODUCTS -> Products()
                        }
                        if (showImage) {
                            Surface(
                                elevation = 5.dp,
                                shape = CircleShape,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Products() {
    val text = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val list = remember { mutableStateListOf<Int>() }

    LaunchedEffect(true) {
        withContext(Dispatchers.Default){
            flowOf(1,
                2,
                3,
                4,
                5,
                4,
                5,
                6,
                7,
                2,
                3,
                4,
                5,
                6,
                7,
                0)
                .collect {
                    list.add(it)
                }
        }
    }


    Column(modifier = Modifier.fillMaxSize()){
        SearchBar(text)
        LazyVerticalGrid(cells = GridCells.Adaptive(140.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)) {

            items(items = list) { item ->
                ProdutoCard(item.toString())
            }
        }
    }
}

@Composable
private fun SearchBar(text: MutableState<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.onBackground,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent
            ),
            modifier = Modifier.weight(8f).padding(16.dp),
            shape = RoundedCornerShape(5),
            placeholder = { Text(text = "Search") },
            trailingIcon = {
                IconButton(onClick = {},
                    modifier = Modifier.pointerHoverIcon(icon = PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))) {
                    Icon(imageVector = Icons.Outlined.Search,
                        contentDescription = null)
                }
            }
        )
        OutlinedButton(onClick = {}, modifier = Modifier.padding(horizontal = 10.dp).height(50.dp)) {
            Icon(imageVector = Icons.Outlined.AddShoppingCart,
                contentDescription = null)
            Text("Add Product", style = MaterialTheme.typography.body1)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ProdutoCard(text: String) {
    Card(
        backgroundColor = Color.White,
        elevation = 5.dp,
        shape = RoundedCornerShape(5)
    ) {
        Column(modifier = Modifier.width(140.dp)) {
            Image(imageVector = Icons.Filled.BabyChangingStation,
                contentDescription = null,
                modifier = Modifier.width(240.dp).height(220.dp).padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Row {
                Text("T-Shirt", modifier = Modifier.weight(1f))
                Text("${text}00.MT", modifier = Modifier.weight(1f))
            }
        }
    }
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



















