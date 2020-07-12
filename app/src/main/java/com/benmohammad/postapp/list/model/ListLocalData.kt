package com.benmohammad.postapp.list.model

import com.benmohammad.core.extensions.performOnBack
import com.benmohammad.core.networking.Scheduler
import com.benmohammad.postapp.commons.data.PostWithUser
import com.benmohammad.postapp.commons.data.local.Post
import com.benmohammad.postapp.commons.data.local.PostDb
import com.benmohammad.postapp.commons.data.local.User
import io.reactivex.Completable
import io.reactivex.Flowable

class ListLocalData(private val postDb: PostDb, private val scheduler: Scheduler): ListDataContract.Local {

    override fun getPostsWithUsers(): Flowable<List<PostWithUser>> {
        return postDb.postDao().getAllPostWithUser()
    }

    override fun saveUsersAndPosts(users: List<User>, posts: List<Post>) {
        Completable.fromAction {
            postDb.userDao().upsertAll(users)
            postDb.postDao().upsertAll(posts)
        }

            .performOnBack(scheduler)
            .subscribe()
    }
}