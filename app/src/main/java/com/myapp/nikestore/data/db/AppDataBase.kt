package com.myapp.nikestore.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.data.repo.source.ProductLocalDataSource

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getProductDao():ProductLocalDataSource
}
