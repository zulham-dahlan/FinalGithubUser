package com.example.githubusers.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.githubusers.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubusers.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.example.githubusers.db.DatabaseContract.UserColumns.Companion._ID
import kotlin.jvm.Throws

class UserHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE,null, values)
    }

    fun deleteById(id : Int): Int{
        return database.delete(DATABASE_TABLE, "$_ID = $id", null)
    }

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context) : UserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserHelper(context)
            }
    }


}