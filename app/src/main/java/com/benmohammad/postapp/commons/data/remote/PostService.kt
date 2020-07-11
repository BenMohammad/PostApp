package com.benmohammad.postapp.commons.data.remote

import com.benmohammad.postapp.commons.data.local.Comment
import com.benmohammad.postapp.commons.data.local.Post
import com.benmohammad.postapp.commons.data.local.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {

    @GET("/posts/")
    fun getPosts(): Single<List<Post>>

    @GET("/users/")
    fun getUsers(): Single<List<User>>

    @GET("/comments/")
    fun getComments(@Query("postId") postId: Int): Single<List<Comment>>
}