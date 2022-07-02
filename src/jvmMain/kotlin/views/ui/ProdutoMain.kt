package views.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.DataUsage
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.rememberDialogState
import components.ScrollableLazylist
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.request.ProdutoRequest
import model.response.Categoria
import model.response.ProdutoResponse
import repository.CategoriaRepository
import repository.ProdutoRepository
import utils.Utils
import views.loadPicture
import java.awt.Cursor
import java.io.File


@Composable
fun ProdutoContainer() {
    val repository = ProdutoRepository.getInstance()!!
    val visible = derivedStateOf {
        repository.totalPages.value != -1
    }
    var currentPage by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            SectionOne()
            Box {
                Box(modifier = Modifier.align(Alignment.TopCenter)) {
                    ProdutoGrid(currentPage = currentPage)
                }
                if (visible.value) {
                    Pagination(
                        repository.totalPages.value,
                        onClick = {
                            scope.launch {
                                repository.pagination(it)
                                delay(2000)
                                currentPage = it
                            }
                        },
                        modifier = Modifier.align(Alignment.BottomCenter).padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionOne() {
    var open by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Products", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Let's grow to your business! Create your product and upload here.", fontSize = 14.sp)
            }
            Button(
                onClick = { open = !open },
                modifier = Modifier.pointerHoverIcon(
                    icon =
                    PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                ),
                shape = RoundedCornerShape(35)
            ) {
                Icon(imageVector = Icons.Default.Add, null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Create Item")
            }
        }
        Divider(modifier = Modifier.fillMaxWidth())
        if (open) {
            CreateItem()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProdutoGrid(currentPage: Int) {
    val repository = ProdutoRepository.getInstance()!!
    val pageIndex by remember { mutableStateOf(currentPage) }
    val page = repository.pageItems

    ScrollableLazylist {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(240.dp),
            state = it,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(32.dp), modifier = Modifier.padding(bottom = 50.dp)
        ) {

            items(items = page) { data ->
                ImageCard(data = data)
            }

        }
    }
}

@Composable
private fun ImageCard(data: ProdutoResponse) {
    val img = data.images.toList()
    val scope = rememberCoroutineScope()
    val image = remember { mutableStateOf(ImageBitmap(0, 0)) }

    val loadImage = scope.async {
        mutableStateOf(loadPicture(url = img[0]))
    }

    scope.launch {
        image.value = loadImage.await().value
    }

    Card(
        modifier = Modifier.width(260.dp).padding(bottom = 16.dp),
        backgroundColor = Color.LightGray
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AnimatedVisibility(visible = image.value.width != 0) {
                Image(
                    bitmap = image.value, null, contentScale = ContentScale.Crop,
                    modifier = Modifier.height(200.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.produto.name,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Cursive
                )

                Text(
                    text = "${data.produto.price}MT",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Cursive,
                    maxLines = 1
                )

            }
        }
    }
}

@Composable
private fun Pagination(
    maxPage: Int = 0,
    onClick: (Int) -> Unit, modifier: Modifier = Modifier,
) {
    var currentPage by remember { mutableStateOf(0) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until maxPage) {
            Button(
                onClick = {
                    currentPage = i
                    onClick(i)
                },
                modifier = Modifier.size(40.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = if (currentPage == i) Color.Gray else Color.White)
            ) {
                Text("$i")
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun CreateItem() {
    val state = rememberDialogState(size = DpSize(800.dp, 400.dp))
    var visible by remember { mutableStateOf(true) }
    val repository = ProdutoRepository.getInstance()!!
    var dialogUpdate by remember { mutableStateOf(false) }
    Dialog(
        state = state,
        visible = visible,
        enabled = true,
        onCloseRequest = { visible = false },
        undecorated = true
    ) {
        val name = remember { mutableStateOf("") }
        val description = remember { mutableStateOf("Description") }
        val price = remember { mutableStateOf("") }
        val quantity = remember { mutableStateOf("") }
        val categoria = remember { mutableStateOf(-1) }
        var msg by remember { mutableStateOf("") }
        var uploadProgress by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val images = mutableStateListOf<File>()

        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background,
        ) {
            Box {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 32.dp).fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 48.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { name.value = it },
                            placeholder = { Text(text = "Produto") },
                            maxLines = 1,
                            shape = RoundedCornerShape(5),
                            modifier = Modifier.weight(1f).size(50.dp)
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            Menu(
                                itemSelected = { categoria.value = it.toInt() },
                                modifier = Modifier
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = price.value,
                            onValueChange = { price.value = it },
                            placeholder = { Text(text = "Price") },
                            maxLines = 1,
                            trailingIcon = {
                                Row {
                                    Icon(imageVector = Icons.Outlined.AttachMoney, null)
                                }
                            }, singleLine = false,
                            shape = RoundedCornerShape(5),
                            modifier = Modifier.weight(1f).size(50.dp)
                        )
                        OutlinedTextField(
                            value = quantity.value,
                            onValueChange = { quantity.value = it },
                            placeholder = { Text(text = "Stock") },
                            maxLines = 1,
                            trailingIcon = {
                                Row {
                                    Icon(imageVector = Icons.Outlined.DataUsage, null)
                                }
                            },
                            shape = RoundedCornerShape(5),
                            modifier = Modifier.weight(1f).size(50.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BasicTextField(
                            value = description.value,
                            onValueChange = { description.value = it },
                            modifier = Modifier.weight(8f).height(50.dp).border(1.dp, color = Color.LightGray),
                        )
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    val files = Utils.chooseMultipleImages()
                                    if (files != null) {
                                        images.clear()
                                        images.addAll(files)
                                    }
                                }
                            },
                            modifier = Modifier.weight(2f).height(50.dp)
                                .pointerHoverIcon(icon = PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                        ) {
                            Text(text = "Imagens")
                            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                            Icon(imageVector = Icons.Outlined.AttachFile, null)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ImageList(images = images)
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                        .padding(vertical = 8.dp, horizontal = 48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Button(
                        onClick = { visible = !visible },
                        modifier = Modifier.weight(1f)
                    ) { Text(text = "Close") }

                    Button(
                        onClick = {
                            scope.launch {
                                uploadProgress = true
                                if (validate(name, categoria, price, quantity, description)) {
                                    val form = ProdutoRequest(
                                        name.value, categoria.value,
                                        price.value.toFloat(), quantity.value.toInt(), description.value
                                    )

                                    val save = repository.save(files = images, produto = form)
                                    dialogUpdate = true
                                    msg = if (save) {
                                        resetForm(name, categoria, price, quantity, description)
                                        "Produto gravado com sucesso!"
                                    } else {
                                        "Erro ao gravar!"
                                    }
                                }
                                uploadProgress = false
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Upload")
                        if (!uploadProgress) {
                            Icon(imageVector = Icons.Outlined.FileUpload, null)
                        } else {
                            CircularProgressIndicator(modifier = Modifier.size(28.dp))
                        }
                    }

                }
                if (dialogUpdate) {
                    Popup(alignment = Alignment.TopStart) {
                        Text(msg, fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlertUpdate(isOpen: Boolean, title: String = "Upload") {
    var state by remember { mutableStateOf(isOpen) }
    if (state) {
        AlertDialog(
            onDismissRequest = { state = false },
            confirmButton = {
                Button(onClick = { state = false }) {
                    Text("Close")
                }
            },
            title = { Text(text = title) }
        )
    }
}

private fun resetForm(
    name: MutableState<String>,
    categoria: MutableState<Int>,
    price: MutableState<String>,
    quantity: MutableState<String>,
    description: MutableState<String>,
) {
    categoria.value = -1
    name.value = ""
    price.value = ""
    quantity.value = ""
    description.value = ""
}

private fun validate(
    name: MutableState<String>,
    categoria: MutableState<Int>,
    price: MutableState<String>,
    quantity: MutableState<String>,
    description: MutableState<String>,
): Boolean {
    return name.value.isNotBlank()
        .and(categoria.value != -1)
        .and(description.value.isNotBlank())
        .and(price.value.isNotBlank())
        .and(quantity.value.isNotBlank())
}


@Composable
fun Menu(modifier: Modifier = Modifier, itemSelected: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("Categoria") }
    val icon = if (open) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
    val menu = CategoriaRepository.getInstance()!!.categorias

    Surface(color = Color.LightGray, shape = RoundedCornerShape(5)) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.body1, modifier = Modifier.weight(9f))
                IconButton(
                    onClick = { open = !open },
                    modifier = Modifier.weight(1f),
                    content = { Icon(imageVector = icon, null) }
                )
            }


            if (open) {
                CategoriaMenu(menu) { id, name ->
                    itemSelected(id)
                    title = name
                    open = false
                }
            }

        }
    }
}

@Composable
private fun CategoriaMenu(menu: SnapshotStateMap<String, Categoria>, onClick: (id: String, name: String) -> Unit) {
    Surface(
        color = Color.Gray, modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            menu.forEach {
                DropdownMenuItem(
                    onClick = {
                        onClick(it.value.id.toString(), it.value.name)
                    },
                    modifier = Modifier.width(200.dp).height(30.dp)
                ) {
                    Row(modifier = Modifier.width(200.dp)) {
                        Text(
                            text = it.value.name,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageList(images: SnapshotStateList<File>) {
    val values = derivedStateOf {
        images.map { loadImageBitmap(it.inputStream()) }
    }
    ScrollableLazylist { state ->
        LazyVerticalGrid(
            cells = GridCells.Adaptive(120.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(32.dp), state = state
        ) {
            items(items = values.value) {
                Image(
                    bitmap = it, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp)
                )
            }
        }

    }
}

