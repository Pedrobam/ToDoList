package com.example.todolist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TodoDatabase : RoomDatabase() {

    abstract val todoDao: TodoDao
}

