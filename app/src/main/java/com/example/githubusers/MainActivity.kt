package com.example.githubusers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)

        getThemeSettings(pref).observe(this,
            { isDarkModeActive ->
                if (isDarkModeActive){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        binding.edtSearch.edtSearchText.doOnTextChanged { text, _, _, _ ->
            searchGithubUser(text.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.favorit -> {
                startActivity(Intent(this,FavoriteActivity::class.java))
                true
            }
            R.id.settings -> {
                startActivity(Intent(this,SettingsActivity::class.java))
                true
            }
            else -> true
        }
    }

    private fun searchGithubUser(username : String){
        showsLoading(true)
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showsLoading(false)
                val responseBody = response.body()
                val listUsers = ArrayList<UserGithub>()
                if (responseBody != null){
                    for (user in responseBody.listUser){
                        listUsers.add(user)
                    }
                    binding.rvGithubuser.layoutManager = LinearLayoutManager(this@MainActivity)
                    val userAdapter = UserAdapter(listUsers)
                    binding.rvGithubuser.adapter = userAdapter
                    userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
                        override fun onItemClicked(data: UserGithub) {
                            val intent = Intent(this@MainActivity, DetailUser::class.java)
                            intent.putExtra(DetailUser.EXTRA_ID, data.id)
                            intent.putExtra(DetailUser.EXTRA_USERNAME, data.username)
                            startActivity(intent)
                        }
                    })
                }else{
                    Toast.makeText(this@MainActivity,"User Not Found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showsLoading(false)
                Toast.makeText(this@MainActivity,"Something Wrong :/n ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showsLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getThemeSettings(pref: SettingPreferences) : LiveData<Boolean> {
        return pref.getThemSetting().asLiveData()
    }
}