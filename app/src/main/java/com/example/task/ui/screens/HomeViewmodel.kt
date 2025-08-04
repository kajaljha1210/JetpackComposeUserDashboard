package com.example.task.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.task.data.AppDatabase
import com.example.task.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val INSERT_DELAY_MS = 500L
private const val BACKGROUND_TASK_DELAY_MS = 5000L

class HomeViewmodel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    val users: LiveData<List<User>> = userDao.getAllUsers()

    val loading = MutableLiveData(false)
    val backgroundProcessing = MutableLiveData(false)
    val message = MutableLiveData("")

    fun addUsers() {
        viewModelScope.launch {
            loading.value = true
            userDao.deleteAll()

            val userList = listOf(
                User(name = "Kajal", age = 24, category = "Developer"),
                User(name = "Ravi", age = 29, category = "Designer"),
                User(name = "Meera", age = 31, category = "Manager")
            )

            for (user in userList) {
                userDao.insertUser(user)
                delay(INSERT_DELAY_MS)
            }

            loading.value = false
        }
    }

    fun startBackgroundTask() {
        viewModelScope.launch {
            backgroundProcessing.value = true
            delay(BACKGROUND_TASK_DELAY_MS)
            backgroundProcessing.value = false
            message.value = "Background Task Completed!"
        }
    }
}
