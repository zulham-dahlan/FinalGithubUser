package com.example.githubusers

import com.google.gson.annotations.SerializedName

data class GithubResponse(
  @field:SerializedName("items")
  val listUser : ArrayList<UserGithub>

)

data class UserGithub(
    @field:SerializedName("id")
    val id: Int?,

    @field:SerializedName("login")
    val username : String?,

    @field:SerializedName("name")
    val fullname : String?,

    @field:SerializedName("company")
    val company : String?,

    @field:SerializedName("location")
    val location : String?,

    @field:SerializedName("avatar_url")
    val avatar : String?,

    @field:SerializedName("public_repos")
    val repository : String?,
)
