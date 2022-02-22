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