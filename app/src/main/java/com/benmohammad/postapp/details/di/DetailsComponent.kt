package com.benmohammad.postapp.details.di

import com.benmohammad.core.networking.Scheduler
import com.benmohammad.postapp.commons.data.local.PostDb
import com.benmohammad.postapp.commons.data.remote.PostService
import com.benmohammad.postapp.details.DetailsActivity
import com.benmohammad.postapp.details.model.DetailsDataContract
import com.benmohammad.postapp.details.model.DetailsLocalData
import com.benmohammad.postapp.details.model.DetailsRemoteData
import com.benmohammad.postapp.details.model.DetailsRepository
import com.benmohammad.postapp.details.viewmodel.DetailsViewModelFactory
import com.benmohammad.postapp.list.di.ListComponent
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@DetailsScope
@Component(dependencies = [ListComponent::class], modules = [DetailsModule::class])
interface DetailsComponent {
    fun inject(detailsActivity: DetailsActivity)
}

@Module
class DetailsModule {

    @Provides
    @DetailsScope
    fun detailsViewModelFactory(repo: DetailsDataContract.Repository, compositeDisposable: CompositeDisposable): DetailsViewModelFactory {
        return DetailsViewModelFactory(repo, compositeDisposable)
    }

    @Provides
    @DetailsScope
    fun detailsRepo(local: DetailsDataContract.Local, remote: DetailsDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable) : DetailsDataContract.Repository =
        DetailsRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    @DetailsScope
    fun remoteData(postService: PostService): DetailsDataContract.Remote = DetailsRemoteData(postService)

    @Provides
    @DetailsScope
    fun localData(postDb: PostDb, scheduler: Scheduler): DetailsDataContract.Local = DetailsLocalData(postDb, scheduler)

    @Provides
    @DetailsScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()


}