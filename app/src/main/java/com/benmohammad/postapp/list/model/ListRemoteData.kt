package com.benmohammad.postapp.list.model

import com.benmohammad.postapp.commons.data.local.Post
import com.benmohammad.postapp.commons.data.local.User
import com.benmohammad.postapp.commons.data.remote.PostService
import io.reactivex.Single

class ListRemoteData(private val postsService: PostService): ListDataContract.Remote {

    override fun getUsers(): Single<List<User>> = postsService.getUsers()

    override fun getPosts(): Single<List<Post>> = postsService.getPosts()
}