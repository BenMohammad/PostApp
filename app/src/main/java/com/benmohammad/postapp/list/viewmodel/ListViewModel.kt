package com.benmohammad.postapp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.benmohammad.core.extensions.toLiveData
import com.benmohammad.core.networking.Outcome
import com.benmohammad.postapp.commons.data.PostWithUser
import com.benmohammad.postapp.list.model.ListDataContract
import io.reactivex.disposables.CompositeDisposable

class ListViewModel(private val repo: ListDataContract.Repository,
                    private val compositeDisposable: CompositeDisposable):ViewModel() {

    val postOutcome: LiveData<Outcome<List<PostWithUser>>> by lazy {
        repo.postFetchOutcome.toLiveData(compositeDisposable)
    }


    fun getPosts() {
        if(postOutcome.value == null)
            repo.fetchPosts()
    }

    fun refreshPosts() {
        repo.refreshPosts()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        //PostDH.destroyListComponent()
    }
}