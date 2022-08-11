package com.example.dropdownmenubox

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.dropdownmenubox.ui.theme.DropDownMenuBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DropDownMenuBoxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val options =
                        listOf("Activity", "Service", "Broadcast Receiver", "Content Provider")
                    DropDownMenuBox("Android Components", options, 0)
                    {
                        Toast.makeText(this, "Component clicked", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownMenuBox(
    title: String,
    items: List<String>,
    selectedMenuItem: Int = 0,
    onclick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var selectedOptionText by remember { mutableStateOf(selectedMenuItem) }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = title)
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(top = 8.dp),
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = !isExpanded
            }
        ) {
            TextField(
                readOnly = true,
                value = items[selectedOptionText],
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isExpanded
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp)),
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        onClick = {
                            isExpanded = false
                            selectedOptionText = index
                            onclick()
                        }) {
                        Text(text = item)
                    }
                    if (index != items.size - 1) {
                        Divider(thickness = 1.dp, color = Color.Black)
                    }
                }
            }
        }
    }
}
