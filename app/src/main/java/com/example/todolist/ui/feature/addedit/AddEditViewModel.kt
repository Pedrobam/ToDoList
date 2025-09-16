package com.example.todolist.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.todolist.data.TodoRepository
import com.example.todolist.navigation.AddEditRoute
import com.example.todolist.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TodoRepository
) : ViewModel() {

    private val addEditRoute = savedStateHandle.toRoute<AddEditRoute>()

    var tittle by mutableStateOf("")
        private set

    var description by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        addEditRoute.id?.let {
            viewModelScope.launch {
                val todo = repository.getBy(it)
                tittle = todo?.title ?: ""
                description = todo?.description
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.TitleChanged -> {
                tittle = event.title
            }

            is AddEditEvent.DescriptionChanged -> {
                description = event.description
            }

            AddEditEvent.Save -> {
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        viewModelScope.launch {
            if (tittle.isBlank()) {
                _uiEvent.send(UiEvent.ShowSnackbar(message = "The title can't be empty"))
                return@launch
            }
            repository.insert(tittle, description, addEditRoute.id)
            _uiEvent.send(UiEvent.NavigateBack)
        }
    }
}