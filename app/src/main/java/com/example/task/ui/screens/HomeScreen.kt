package com.example.task.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task.model.User

import androidx.compose.material3.Switch
import androidx.compose.ui.graphics.Color



@Composable
fun HomeScreen() {
    val viewModel: HomeViewmodel = viewModel()
    val users by viewModel.users.observeAsState(emptyList())
    val isLoading by viewModel.loading.observeAsState(false)
    val isBackgroundProcessing by viewModel.backgroundProcessing.observeAsState(false)
    val message by viewModel.message.observeAsState("")

    val isDarkTheme = remember { mutableStateOf(false) }

    val uiState = UiState(
        users = users,
        isLoading = isLoading,
        isBackgroundProcessing = isBackgroundProcessing,
        message = message
    )

    val colorScheme = if (isDarkTheme.value) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    isDarkTheme = isDarkTheme.value,
                    onThemeToggle = { isDarkTheme.value = it }
                )
            },
            content = { paddingValues ->
                HomeScreenContent(
                    modifier = Modifier.padding(paddingValues),
                    uiState = uiState,
                    onAddUsersClick = viewModel::addUsers,
                    onStartTaskClick = viewModel::startBackgroundTask
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    TopAppBar(
        title = { Text(text = "User Dashboard") },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.DarkGray
                    )
                )
            }
        }
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAddUsersClick: () -> Unit,
    onStartTaskClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ActionButtons(
            onAddUsersClick = onAddUsersClick,
            onStartTaskClick = onStartTaskClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            UserList(uiState.users)
        }

        if (uiState.isBackgroundProcessing) {
            Text("Background task is running...")
        }

        if (uiState.message.isNotEmpty()) {
            Text(uiState.message)
        }
    }
}

@Composable
fun ActionButtons(
    onAddUsersClick: () -> Unit,
    onStartTaskClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onAddUsersClick) {
            Text(text = "Add Users")
        }
        Button(onClick = onStartTaskClick) {
            Text(text = "Start Task")
        }
    }
}

@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            UserCard(user)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${user.name}")
            Text(text = "Age: ${user.age}")
            Text(text = "Category: ${user.category}")
        }
    }
}
