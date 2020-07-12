package com.benmohammad.postapp.list.model

import com.benmohammad.core.networking.Outcome
import com.benmohammad.postapp.commons.data.PostWithUser
import com.benmohammad.postapp.commons.data.local.Post
import com.benmohammad.postapp.commons.data.local.User
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface ListDataContract {

    interface Repository {
        val postFetchOutcome: PublishSubject<Outcome<List<PostWithUser>>>
        fun fetchPosts()
        fun refreshPosts()
        fun saveUsersAndPosts(users: List<User>, posts: List<Post>)
        fun handleError(error: Throwable)
    }

    interface Local {
        fun getPostsWithUsers(): Flowable<List<PostWithUser>>
        fun saveUsersAndPosts(users: List<User>, posts: List<Post>)
    }

    interface Remote {
        fun getUsers(): Single<List<User>>
        fun getPosts(): Single<List<Post>>
    }
}