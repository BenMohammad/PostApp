package com.benmohammad.postapp.details.model

import com.benmohammad.core.networking.Outcome
import com.benmohammad.postapp.commons.data.local.Comment
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface DetailsDataContract {

    interface Repository {
        val commentsFetchOutcome: PublishSubject<Outcome<List<Comment>>>
        fun fetchCommentsFor(postId: Int?)
        fun refreshComments(postId: Int)
        fun saveCommentsForPost(comments: List<Comment>)
        fun handleError(error: Throwable)
    }

    interface Local {
        fun getCommentsForPost(postId: Int): Flowable<List<Comment>>
        fun saveComments(comments: List<Comment>)
    }

    interface Remote {
        fun getCommentsForPost(postId: Int): Single<List<Comment>>

    }}