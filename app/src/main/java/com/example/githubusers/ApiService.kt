package com.example.githubusers

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUser(
        @Query("q") login: String
    ): Call<GithubResponse>

    @GET("users/{login}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("login") login: String
    ): Call<UserGithub>

    @GET("users/{login}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("login") login: String
    ): Call<ArrayList<UserGithub>>

    @GET("users/{login}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("login") login: String
    ): Call<ArrayList<UserGithub>>


}