package com.mabn.taskia.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.MessageType
import com.mabn.taskia.domain.util.toDp
import com.mabn.taskia.ui.common.AlertButton
import com.mabn.taskia.ui.common.TopBar
import com.mabn.taskia.ui.taskForm.TaskFormViewModel
import com.mabn.taskia.ui.taskForm.view.TaskForm
import com.mabn.taskia.ui.taskList.view.TaskEntireList
import com.mabn.taskia.ui.theme.DoItTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun MainView(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val taskFormViewModel: TaskFormViewModel = hiltViewModel()
    val currentList = viewModel.currentList.collectAsState()
    val formDataChanged = taskFormViewModel.dataChanged.collectAsState()
    val showTaskChangedDialog = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val keyboardHeight = viewModel.keyboardHeight.observeAsState()
    val isLandscape = viewModel.isLandscape.observeAsState()

    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    keyboardController?.hide()
                    showTaskChangedDialog.value = false
                    taskFormViewModel.isVisible.value = false
                    if (formDataChanged.value) {
                        showTaskChangedDialog.value = true
                    }
                } else {
                    showTaskChangedDialog.value = false
                }
                if (isLandscape.value == true && it == ModalBottomSheetValue.HalfExpanded) {
                    false
                } else {
                    !formDataChanged.value
                            || it != ModalBottomSheetValue.Hidden
                }
            })
    val offsetState =
        animateIntAsState(
            targetValue = if (
                modalBottomSheetState.progress.to == ModalBottomSheetValue.HalfExpanded &&
                keyboardHeight.value != null &&
                keyboardHeight.value != 0
            ) {
                val value =
                    if (isLandscape.value != true)
                        keyboardHeight.value!!.toDp + 350.toDp - (configuration.screenHeightDp / 2) else 0
                if (value > 0) value else 0
            } else 0
        )
    // Handle displaying snackbar if any message
    val messageState = viewModel.message.observeAsState()

    LaunchedEffect(messageState.value) {
        if (messageState.value != null
            && (messageState.value!!.type == MessageType.SNACKBAR || messageState.value!!.type == MessageType.TOAST)
        ) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                val message = messageState.value!!
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = message.text,
                    actionLabel = message.actionText,
                    duration = if (message.type == MessageType.SNACKBAR) SnackbarDuration.Short else SnackbarDuration.Short
                )
                when (snackbarResult) {
                    SnackbarResult.ActionPerformed -> {
                        message.actionFun.invoke()
                    }
                    SnackbarResult.Dismissed -> {
                        viewModel.clearMessage()
                    }
                }
                viewModel.clearMessage()
            }
        }
    }

    LaunchedEffect(currentList.value) {
        taskFormViewModel.setCurrentList(currentList.value)
    }


    val hideBottomSheet = {
        coroutineScope.launch(Dispatchers.Main) {
            modalBottomSheetState.hide()
            keyboardController?.hide()
        }
        taskFormViewModel.isVisible.value = false
    }

    val context = LocalContext.current
    BackHandler(scaffoldState.drawerState.isOpen) {
        coroutineScope.launch(Dispatchers.Main) {
            scaffoldState.drawerState.close()
        }
    }
    BackHandler(modalBottomSheetState.isVisible) {
        coroutineScope.launch(Dispatchers.Main) {
            if (formDataChanged.value) {
                showTaskChangedDialog.value = true
            }
            hideBottomSheet()
        }
    }
    BackHandler(!modalBottomSheetState.isVisible) {
        (context as? Activity)?.finish()
    }
    DoItTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            ModalBottomSheetLayout(
                modifier = Modifier.offset(y = (-offsetState.value).dp),
                sheetContent = {
                    Column(
                        modifier = Modifier
                            .requiredHeight(
                                if (isLandscape.value == true) configuration.screenHeightDp.dp
                                else (configuration.screenHeightDp - 50).dp
                            )
                    ) {
                        TaskForm(taskFormViewModel) {
                            coroutineScope.launch(Dispatchers.Main) {
                                taskFormViewModel.isVisible.value = false
                                modalBottomSheetState.hide()
                            }
                        }
                    }
                },
                sheetState = modalBottomSheetState,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                sheetBackgroundColor = MaterialTheme.colors.background,
            ) {
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        Box(
                            modifier = Modifier.offset(y = offsetState.value.dp),
                        ) {
                            TopBar(
                                tabs = viewModel.availableListSet.map {
                                    Pair(stringResource(id = it.textId)) {
                                        viewModel.showList(it)
                                    }
                                }
                            ) {
                                coroutineScope.launch(Dispatchers.Main) {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        }
                    },
                    /*
                    drawerContent = {
                        Drawer {
                            // TODO
                        }
                    },
                     */
                    floatingActionButton = {
                        FloatingActionButton(
                            backgroundColor = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .testTag("add_task_button")
                                .offset(y = offsetState.value.dp),
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    taskFormViewModel.clear()
                                    taskFormViewModel.isVisible.value = true
                                    modalBottomSheetState.show()
                                }
                            }) {
                            Icon(Icons.Filled.Add, "")
                        }
                    }, snackbarHost = {
                        SnackbarHost(hostState = scaffoldState.snackbarHostState)
                    }) {
                    Box(modifier = Modifier.offset(y = offsetState.value.dp)) {
                        TaskEntireList(
                            viewModel = hiltViewModel(),
                            listType = currentList.value,
                            showTaskForm = { task ->
                                coroutineScope.launch(Dispatchers.Main) {
                                    taskFormViewModel.setTask(task, context = context)
                                    taskFormViewModel.isVisible.value = true
                                    //modalBottomSheetState.show()
                                    modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }
                            }
                        )
                    }
                }
            }

        }

        if (showTaskChangedDialog.value) {

            NotSavedAlert(saveFun = {
                taskFormViewModel.saveTask()
                hideBottomSheet()
                showTaskChangedDialog.value = false
            }, discardFun = {
                hideBottomSheet()
                showTaskChangedDialog.value = false
            }, dismissFun = {
                showTaskChangedDialog.value = false
            })
        }
    }
}

@Composable
private fun NotSavedAlert(saveFun: () -> Unit, dismissFun: () -> Unit, discardFun: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            dismissFun()
        },
        properties = DialogProperties(),
        title = {
            Text(stringResource(id = R.string.unsaved_changes))
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AlertButton(
                    stringResource(id = R.string.discard),
                    onClick = {
                        discardFun()
                    },
                    modifier = Modifier.padding(end = 10.dp)
                )
                AlertButton(
                    stringResource(id = R.string.save),
                    onClick = { saveFun() }
                )
            }


        }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun MainView_Preview() {
    MaterialTheme {
        MainView(hiltViewModel())
    }
}