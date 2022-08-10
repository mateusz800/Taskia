package com.mabn.taskia.ui.common.base

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.repository.TagRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class WithFilter(private val tagRepository: TagRepository) : ViewModel() {
    private val _filterTags = MutableLiveData<List<Tag>>()
    val filterTags: LiveData<List<Tag>> = _filterTags

    private val _allTags = MutableLiveData<List<Tag>>()
    val allTags: LiveData<List<Tag>> = _allTags

    val tasks: LiveData<SnapshotStateList<Pair<Task, List<Task>>>>
        get() = filteredTasks
    protected val filteredTasks = MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()


    open fun setFilterTags(tags: List<Tag>) {
        _filterTags.postValue(tags)
    }

    protected fun collectTags() {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.getAll().collect {
                _allTags.postValue(it)
            }
        }
    }

    protected fun filter(
        list: List<Pair<Task, Pair<List<Task>, List<Tag>>>>?,
        targetLiveData: MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>
    ) {
        val snapshotList = SnapshotStateList<Pair<Task, List<Task>>>()
        snapshotList.addAll(filterByTags(list))
        targetLiveData.postValue(snapshotList)
    }

    private fun filterByTags(list: List<Pair<Task, Pair<List<Task>, List<Tag>>>>?): List<Pair<Task, List<Task>>> {
        if (_filterTags.value.isNullOrEmpty()) {
            return list?.map {
                Pair(it.first, it.second.first)
            } ?: listOf()
        }
        return list?.filter {
            it.second.second.intersect(_filterTags.value!!.toSet()).size == _filterTags.value!!.size
        }?.map {
            Pair(it.first, it.second.first)
        }?.toList() ?: listOf()
    }
}