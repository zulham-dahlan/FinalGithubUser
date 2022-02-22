package com.example.githubusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.databinding.FragmentFollowerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {

    private var _binding : FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_USERNAME = "arg_username"

        @JvmStatic
        fun newInstance(username: String) : FollowerFragment{
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val queryUsername = arguments?.getString(ARG_USERNAME)
        displayFollower(queryUsername!!)
    }


    private fun displayFollower(username : String){
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object: Callback<ArrayList<UserGithub>> {
            override fun onResponse(
                call: Call<ArrayList<UserGithub>>,
                response: Response<ArrayList<UserGithub>>
            ) {
                val responseBody = response.body()
                if (responseBody != null){
                    binding.rvFollower.layoutManager = LinearLayoutManager(activity)
                    val userAdapter = UserAdapter(responseBody)
                    binding.rvFollower.adapter = userAdapter

                    userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
                        override fun onItemClicked(data: UserGithub) {
                            Toast.makeText(activity, data.username, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<ArrayList<UserGithub>>, t: Throwable) {
                Toast.makeText(activity,"Something Wrong :/n ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}