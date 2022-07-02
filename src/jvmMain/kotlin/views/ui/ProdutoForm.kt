package views.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Formulario() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF7F7F7)) {

            Column(modifier = Modifier.padding(top = 16.dp, start = 120.dp, end = 64.dp, bottom = 32.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Add product", style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 18.sp
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(onClick = {},
                            shape = RoundedCornerShape(10),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            content = {
                                Text(
                                    text = "Save to draft", fontWeight = FontWeight.Bold
                                )
                            })

                        Button(onClick = {},
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3167EB)),
                            shape = RoundedCornerShape(10),
                            content = {
                                Text(
                                    text = "Publish now", fontWeight = FontWeight.Bold, color = Color.White
                                )
                            })
                    }

                }
                Row {
                    Column(modifier = Modifier.weight(7f)) {
                        Card(
                            elevation = 0.dp,
                            shape = RoundedCornerShape(4),
                            modifier = Modifier.height(240.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {

                                OutlinedTextField("",
                                    onValueChange = {},
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Product title") },
                                    placeholder = {
                                        Text(
                                            "Type", color = Color(0xFFD6D6D6)
                                        )
                                    })

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField("",
                                        onValueChange = {},
                                        modifier = Modifier.weight(3f),
                                        label = { Text("SKU") },
                                        placeholder = {
                                            Text(
                                                "Type", color = Color(0xFFD6D6D6)
                                            )
                                        })
                                    OutlinedTextField("",
                                        onValueChange = {},
                                        modifier = Modifier.weight(3f),
                                        label = { Text("Color") },
                                        placeholder = {
                                            Text(
                                                "Type", color = Color(0xFFD6D6D6)
                                            )
                                        })
                                    OutlinedTextField("",
                                        onValueChange = {},
                                        modifier = Modifier.weight(3f),
                                        label = { Text("Size") },
                                        placeholder = {
                                            Text(
                                                "Type", color = Color(0xFFD6D6D6)
                                            )
                                        })
                                }


                                OutlinedTextField("",
                                    onValueChange = {},
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Size") },
                                    placeholder = {
                                        Text(
                                            "Type", color = Color(0xFFD6D6D6)
                                        )
                                    })
                            }

                        }
                    }
                    Spacer(modifier = Modifier.weight(0.5f))
                    Column(modifier = Modifier.weight(2.4f)) {

                        Card(
                            elevation = 0.dp,
                            modifier = Modifier.height(300.dp)
                        ) {
                            Column() {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text("Price")
                                    OutlinedTextField(
                                        "", onValueChange = {},
                                        shape = RoundedCornerShape(15),
                                        placeholder = { Text("0.00") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .size(50.dp)
                                    )
                                }
                                Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp))

                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text("Status", modifier = Modifier)
                                    OutlinedTextField(
                                        "",
                                        onValueChange = {},
                                        shape = RoundedCornerShape(1),
                                        placeholder = { Text("Activate") },
                                        trailingIcon = { Icon(imageVector = Icons.Default.ArrowDropDown, null) },
                                        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().size(50.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}