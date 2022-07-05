package com.mabn.taskia.ui.taskList

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Rule
import javax.inject.Inject


class TaskListViewModelTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var viewModel:TaskListViewModel
}