package com.example.doit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.doit.ui.MainViewModel
import com.example.doit.ui.browseTasks.view.TaskList
import com.example.doit.ui.taskForm.view.TaskForm
import com.example.doit.ui.theme.DoItTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun MainView(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    DoItTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            ModalBottomSheetLayout(
                sheetContent = {
                    TaskForm(hiltViewModel()) {
                        coroutineScope.launch(Dispatchers.Main) { modalBottomSheetState.hide() }
                    }

                },
                sheetState = modalBottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                sheetBackgroundColor = MaterialTheme.colors.background,
            ) {
                Scaffold(floatingActionButton = {
                    FloatingActionButton(onClick = {
                        coroutineScope.launch(Dispatchers.Main) {
                            modalBottomSheetState.show()
                        }
                    }) {
                        Icon(Icons.Filled.Add, "")
                    }
                }) {
                    TaskList(viewModel = hiltViewModel())
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun MainView_Preview() {
    MaterialTheme {
        MainView(hiltViewModel())
    }
}