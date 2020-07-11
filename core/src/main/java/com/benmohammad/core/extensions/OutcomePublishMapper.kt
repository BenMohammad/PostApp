package com.benmohammad.core.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benmohammad.core.networking.Outcome
import com.benmohammad.core.networking.Outcome.Companion.failure
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

fun <T> PublishSubject<T>.toLiveData(compositeDisposable: CompositeDisposable): LiveData<T> {
    val data = MutableLiveData<T>()
    compositeDisposable.add(this.subscribe{ t: T -> data.value = t})
    return data
}

fun <T> PublishSubject<Outcome<T>>.failed(e: Throwable) {
    with(this) {
        loading(false)
        onNext(failure(e))
    }
}

fun <T> PublishSubject<Outcome<T>>.success(t: T) {
    with(this) {
        loading(false)
        onNext(Outcome.success(t))
    }
}

fun <T> PublishSubject<Outcome<T>>.loading(isLoading: Boolean) {
    this.onNext(Outcome.loading(isLoading))
}