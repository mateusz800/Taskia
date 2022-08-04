package com.mabn.taskia.ui.settings

sealed class SettingsEvent{
    data class ActivityChanged(val activity:SettingsActivityEnum): SettingsEvent()
}
