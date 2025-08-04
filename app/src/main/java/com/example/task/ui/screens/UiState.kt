package com.example.task.ui.screens

import com.example.task.model.User

data class UiState(
    val users: List<User>,
    val isLoading: Boolean,
    val isBackgroundProcessing: Boolean,
    val message: String
)

