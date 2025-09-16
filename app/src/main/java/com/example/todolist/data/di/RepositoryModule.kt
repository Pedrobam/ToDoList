package com.example.todolist.data.di

import com.example.todolist.data.TodoRepository
import com.example.todolist.data.TodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bindTodoRepository(todoRepositoryImpl: TodoRepositoryImpl): TodoRepository

}