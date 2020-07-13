package com.benmohammad.postapp.details.model

import com.benmohammad.postapp.commons.data.local.Comment
import com.benmohammad.postapp.commons.data.remote.PostService
import io.reactivex.Single

class DetailsRemoteData(private val postService: PostService): DetailsDataContract.Remote {

    override fun getCommentsForPost(postId: Int): Single<List<Comment>> = postService.getComments(postId)
}