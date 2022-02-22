package com.example.githubusers

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubusers.databinding.ActivityDetailUserBinding
import com.example.githubusers.db.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.example.githubusers.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.example.githubusers.db.DatabaseContract.UserColumns.Companion._ID
import com.example.githubusers.db.UserHelper
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUser : AppCompatActivity() {

    private lateinit var binding : ActivityDetailUserBinding
    private lateinit var userHelper: UserHelper
    private lateinit var user: UserGithub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID,0)
        getUserData(username!!)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        sectionsPagerAdapter.username = username
        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        saveUserFavorite(id)
    }

    private fun getUserData(username: String){
        showsLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserGithub>{
            override fun onResponse(call: Call<UserGithub>, response: Response<UserGithub>) {
                showsLoading(false)
                val responseBody = response.body()
                if (responseBody != null){
                    user = responseBody
                    setUserData()
                }
            }

            override fun onFailure(call: Call<UserGithub>, t: Throwable) {
                showsLoading(false)
                Toast.makeText(this@DetailUser, "Something Wrong : /n ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUserData(){
        with(binding){
            tvUsername.text = user.username
            tvFullname.text = user.fullname ?: "-"
            tvRepository.text = getString(R.string.repository, user.repository ?: "-")
            tvCompany.text = user.company ?: "-"
            tvLocation.text = user.location ?: "-"

            Glide.with(this@DetailUser).load(user.avatar).into(imgProfile)
        }
    }

    private fun showsLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun saveUserFavorite(id : Int){
        val cursor = userHelper.queryById(id.toString())
        var statusFav = cursor.count != 0

        setFavoriteFab(statusFav)
        if (!statusFav){
            binding.fabFavorite.setOnClickListener {
                statusFav = !statusFav
                val contentValue = ContentValues()
                contentValue.put(_ID,user.id)
                contentValue.put(USERNAME,user.username)
                contentValue.put(AVATAR_URL,user.avatar)
                val stt = userHelper.insert(contentValue)
                if (stt>0){
                    Toast.makeText(this, "Tersimpan difavorit", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Gagal di simpan difavorit", Toast.LENGTH_SHORT).show()
                }
                setFavoriteFab(statusFav)
            }
        }else{
            binding.fabFavorite.setOnClickListener {
                statusFav = !statusFav
                val stt = userHelper.deleteById(id)
                if (stt>0){
                    Toast.makeText(this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Gagal dihapus", Toast.LENGTH_SHORT).show()
                }
                setFavoriteFab(statusFav)
            }
        }
    }

    private fun setFavoriteFab(status : Boolean){
        if (status){
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }

}