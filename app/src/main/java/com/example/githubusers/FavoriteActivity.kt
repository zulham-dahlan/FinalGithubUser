package com.example.githubusers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.databinding.ActivityFavoriteBinding
import com.example.githubusers.db.UserHelper
import com.example.githubusers.helper.MappingHelper

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userHelper: UserHelper
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)

        val cursor = userHelper.queryAll()
        userAdapter = UserAdapter(MappingHelper.mapCursorToArrayList(cursor))
        if (cursor.count != 0){
            binding.rvFavorite.adapter = userAdapter
        }else{
            Toast.makeText(this, "List Favorit Kosong", Toast.LENGTH_SHORT).show()
        }
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserGithub) {
                val intent = Intent(this@FavoriteActivity, DetailUser::class.java)
                intent.putExtra(DetailUser.EXTRA_ID, data.id)
                intent.putExtra(DetailUser.EXTRA_USERNAME, data.username)
                startActivity(intent)
            }
        })
        userHelper.close()
    }
}