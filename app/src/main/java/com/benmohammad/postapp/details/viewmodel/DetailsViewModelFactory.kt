package com.benmohammad.postapp.details.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benmohammad.postapp.details.model.DetailsDataContract
import io.reactivex.disposables.CompositeDisposable

@SuppressLint("UNCHECKED_CAST")
class DetailsViewModelFactory(private val repository: DetailsDataContract.Repository, private val compositeDisposable: CompositeDisposable)
    :ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository, compositeDisposable) as T
    }
}