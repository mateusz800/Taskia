package com.mabn.taskia.ui.topBar.filterDropdownMenu

import com.mabn.taskia.domain.model.Tag

sealed class FilterMenuEvent {
    data class TagSelected(val tag: Tag) : FilterMenuEvent()
    object TagsCleared : FilterMenuEvent()
}
