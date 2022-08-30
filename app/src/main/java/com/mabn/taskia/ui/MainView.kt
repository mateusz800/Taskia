package com.mabn.taskia.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.TweenSpec
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.mabn.taskia.domain.model.MessageType
import com.mabn.taskia.domain.util.extension.toDp
import com.mabn.taskia.ui.calendar.CalendarView
import com.mabn.taskia.ui.common.startEditFormActivity
import com.mabn.taskia.ui.taskForm.TaskFormViewModel
import com.mabn.taskia.ui.taskForm.TaskSimpleForm
import com.mabn.taskia.ui.taskForm.components.NotSavedAlert
import com.mabn.taskia.ui.taskList.ListType
import com.mabn.taskia.ui.taskList.TaskList
import com.mabn.taskia.ui.taskList.TaskListViewModel
import com.mabn.taskia.ui.theme.TaskiaTheme
import com.mabn.taskia.ui.topBar.TopBar
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
    val taskListViewModel: TaskListViewModel = hiltViewModel()
    val currentList = viewModel.currentList.collectAsState()
    val formState = taskFormViewModel.formState.observeAsState()
    val showTaskChangedDialog = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val keyboardHeight = viewModel.keyboardHeight.observeAsState()
    val keyboardDismiss = viewModel.keyboardDismiss.observeAsState()
    val isLandscape = viewModel.isLandscape.observeAsState()

    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    keyboardController?.hide()
                    if (formState.value?.dataChanged == true) {
                        showTaskChangedDialog.value = true
                    }
                }
                if (it == ModalBottomSheetValue.Expanded) {
                    return@rememberModalBottomSheetState false
                }
                formState.value?.dataChanged != true
                        || it != ModalBottomSheetValue.Hidden
            }
        )
    val offsetState =
        animateIntAsState(
            targetValue = if (
                modalBottomSheetState.progress.to == ModalBottomSheetValue.HalfExpanded &&
                keyboardHeight.value != null &&
                keyboardHeight.value != 0
            ) {
                val value =
                    if (isLandscape.value != true)
                        keyboardHeight.value!!.toDp + 350.toDp - configuration.screenHeightDp / 2 else 0
                if (value > 0) value else 0
            } else 0
        )
    // Handle displaying snackbar if any message
    val messageState = viewModel.message.observeAsState()
    val hideBottomSheet = {
        coroutineScope.launch(Dispatchers.Main) {
            modalBottomSheetState.hide()
            keyboardController?.hide()
            viewModel.onKeyboardHeightChanged(0, isLandscape.value ?: false)
            if (formState.value?.dataChanged == true) {
                showTaskChangedDialog.value = true
            }
            taskFormViewModel.isVisible.value = false
        }
    }

    val context = LocalContext.current


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

    LaunchedEffect(keyboardDismiss.value, modalBottomSheetState.currentValue) {
        if (modalBottomSheetState.currentValue == ModalBottomSheetValue.HalfExpanded && keyboardDismiss.value == true && !taskFormViewModel.timeDialogOpened) {
            hideBottomSheet()
        }
    }





    BackHandler(enabled = true) {
        if (modalBottomSheetState.isVisible) {
            coroutineScope.launch(Dispatchers.Main) {
                if (formState.value?.dataChanged == true) {
                    showTaskChangedDialog.value = true
                }
                hideBottomSheet()
            }
        } else {
            (context as? Activity)?.finish()
        }
    }

    TaskiaTheme {
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
                        TaskSimpleForm(taskFormViewModel, modalBottomSheetState) {
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
                                viewModel = hiltViewModel(),
                                taskListViewModel = hiltViewModel(),
                                tabs = viewModel.availableListSet.map {
                                    Pair(Pair(stringResource(id = it.textId), it.icon)) {
                                        viewModel.showList(it)
                                    }
                                }
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            backgroundColor = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .testTag("add_task_button"),
                            //.offset(y = offsetState.value.dp),
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    keyboardController?.show()
                                    taskFormViewModel.clear()
                                    taskFormViewModel.isVisible.value = true
                                    showTaskChangedDialog.value = false
                                    modalBottomSheetState.animateTo(
                                        ModalBottomSheetValue.HalfExpanded,
                                        anim = TweenSpec(delay = 200)
                                    )
                                }
                            }) {
                            Icon(Icons.Filled.Add, "")
                        }
                    }, snackbarHost = {
                        SnackbarHost(hostState = scaffoldState.snackbarHostState)
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            //.offset(y = offsetState.value.dp)
                            .padding(padding)
                    ) {

                        if (currentList.value == ListType.Calendar) {
                            CalendarView(
                                calendarViewModel = hiltViewModel(),
                                taskListViewModel = taskListViewModel
                            )
                        } else {
                            TaskList(
                                viewModel = taskListViewModel,
                                listType = currentList.value,
                                showTaskForm = { task ->
                                    startEditFormActivity(context, task)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showTaskChangedDialog.value) {
            NotSavedAlert(saveFun = {
                taskFormViewModel.saveTask()
                taskFormViewModel.clear()
                hideBottomSheet()
                showTaskChangedDialog.value = false
            }, discardFun = {
                hideBottomSheet()
                taskFormViewModel.clear()
                showTaskChangedDialog.value = false
            }, dismissFun = {
                showTaskChangedDialog.value = false
            })
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