package com.mabn.taskia.ui.topBar

sealed class TopBarEvent {
    data class TabChanged(val index: Int) : TopBarEvent()
    data class ToggleMenu(val forceDismiss:Boolean = false) : TopBarEvent()
    data class ToggleFilterMenu(val forceDismiss: Boolean = false) : TopBarEvent()
}
