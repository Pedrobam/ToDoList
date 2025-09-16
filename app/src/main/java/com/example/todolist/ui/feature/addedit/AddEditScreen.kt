package com.example.todolist.ui.feature.addedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.todolist.ui.UiEvent
import com.example.todolist.ui.theme.TodoListTheme

@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val title = viewModel.tittle
    val description = viewModel.description

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent) {
                is UiEvent.Navigate<*> -> {

                }
                UiEvent.NavigateBack -> {
                    navigateBack.invoke()
                }
                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = uiEvent.message
                    )
                }
            }
        }
    }

    AddEditContent(
        title = title,
        description = description,
        onEvent = viewModel::onEvent,
        snackBarHostState
    )
}

@Composable
fun AddEditContent(
    title: String,
    description: String?,
    onEvent: (event: AddEditEvent) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent.invoke(AddEditEvent.Save)
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = {
                    onEvent.invoke(
                        AddEditEvent.TitleChanged(it)
                    )
                },
                placeholder = {
                    Text(text = "Title")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description ?: "",
                onValueChange = {
                    onEvent.invoke(
                        AddEditEvent.DescriptionChanged(it)
                    )
                },
                placeholder = {
                    Text(text = "Description (optional)")
                }
            )
        }
    }
}


@Preview
@Composable
private fun AddEditContentPreview() {
    TodoListTheme {
        AddEditContent(
            title = "",
            description = null,
            onEvent = {},
            snackBarHostState = SnackbarHostState()
        )
    }
}