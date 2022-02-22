package com.example.githubusers.helper

import android.database.Cursor
import com.example.githubusers.UserGithub
import com.example.githubusers.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<UserGithub>{
        val favoriteList = ArrayList<UserGithub>()

        userCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))

                favoriteList.add(UserGithub(id, username,null,null,null,avatar,null))
            }
        }
        return favoriteList
    }
}