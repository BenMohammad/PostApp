package com.benmohammad.postapp.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.benmohammad.core.extensions.toLiveData
import com.benmohammad.core.networking.Outcome
import com.benmohammad.postapp.commons.data.local.Comment
import com.benmohammad.postapp.details.model.DetailsDataContract
import io.reactivex.disposables.CompositeDisposable

class DetailsViewModel(private val repo: DetailsDataContract.Repository, private val compositeDisposable: CompositeDisposable): ViewModel() {

    val commentsOutcome: LiveData<Outcome<List<Comment>>> by lazy {
        repo.commentsFetchOutcome.toLiveData(compositeDisposable)
    }

    fun loadCommentsFor(postId: Int?) {
        repo.fetchCommentsFor(postId)
    }

    fun refreshCommentsFor(postId: Int?) {
        if(postId != null)
            repo.refreshComments(postId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        //PostH.destroyDetailsComponent()
    }
}