package com.example.githubusers.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class UserColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorit_user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"
        }
    }
}