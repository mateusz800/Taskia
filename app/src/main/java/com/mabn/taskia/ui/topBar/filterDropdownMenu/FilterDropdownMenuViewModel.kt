package com.mabn.taskia.ui.topBar.filterDropdownMenu

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mabn.taskia.domain.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterDropdownMenuViewModel @Inject constructor() : ViewModel() {
    private val _tags = MutableLiveData<SnapshotStateList<Pair<Tag, Boolean>>>()
    val tags: LiveData<SnapshotStateList<Pair<Tag, Boolean>>> = _tags

    fun setTags(tags: List<Tag>?) {
        val list = SnapshotStateList<Pair<Tag, Boolean>>()
        tags?.forEach {
            list.add(Pair(it, false))
        }
        _tags.postValue(list)
    }

    fun onEvent(event: FilterMenuEvent){
        when(event){
            is FilterMenuEvent.TagsCleared -> clearSelectedTags()
            is FilterMenuEvent.TagSelected -> selectTag(event.tag)
        }
    }

    fun selectTag(tag: Tag) {

        val tagsList = _tags.value
        val tagPair = tagsList?.first { it.first == tag }
        if (tagsList != null && tagPair != null) {
            tagsList[tagsList.indexOf(tagPair)] = Pair(tagPair.first, !tagPair.second)
            _tags.value = tagsList
        }
    }

    fun getSelectedTags(): List<Tag> {
        return _tags.value?.filter { it.second }?.map { it.first } ?: listOf()
    }

    fun clearSelectedTags() {
        val list = SnapshotStateList<Pair<Tag, Boolean>>()
        _tags.value?.map { Pair(it.first, false) }?.let { list.addAll(it) }
        _tags.value = list
    }
}