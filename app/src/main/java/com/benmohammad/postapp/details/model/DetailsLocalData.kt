package com.benmohammad.postapp.details.model

import com.benmohammad.core.extensions.performOnBack
import com.benmohammad.core.networking.Scheduler
import com.benmohammad.postapp.commons.data.local.Comment
import com.benmohammad.postapp.commons.data.local.PostDb
import io.reactivex.Completable
import io.reactivex.Flowable

class DetailsLocalData(private val postDb: PostDb, private val scheduler: Scheduler): DetailsDataContract.Local {


    override fun getCommentsForPost(postId: Int): Flowable<List<Comment>> {
        return postDb.commentDao().getForPost(postId)
    }

    override fun saveComments(comments: List<Comment>) {
        Completable.fromAction {
            postDb.commentDao().upsertAll(comments)
        }

            .performOnBack(scheduler)
            .subscribe()
    }
}