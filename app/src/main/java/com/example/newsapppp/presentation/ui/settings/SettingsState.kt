package com.example.newsapppp.presentation.ui.settings

import com.example.newsapppp.core.State

sealed class SettingsState : State {

    data class IsSwitch(val isSwitch: Boolean) : SettingsState()

    data class Account(
        val message: String,
        val isError: Boolean = false,
        val action: () -> Unit = {}
    ) : SettingsState()

    data class Account2(val navigation: Int) : SettingsState()

    data class GetCurrentEmail(val currentEmail: String?): SettingsState()
}
