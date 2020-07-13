package com.benmohammad.postapp.list.di

import android.content.Context
import androidx.room.Room
import com.benmohammad.core.constants.Constants
import com.benmohammad.core.di.CoreComponent
import com.benmohammad.core.networking.Scheduler
import com.benmohammad.postapp.commons.data.local.PostDb
import com.benmohammad.postapp.commons.data.remote.PostService
import com.benmohammad.postapp.list.ListActivity
import com.benmohammad.postapp.list.PostListAdapter
import com.benmohammad.postapp.list.model.ListDataContract
import com.benmohammad.postapp.list.model.ListLocalData
import com.benmohammad.postapp.list.model.ListRemoteData
import com.benmohammad.postapp.list.model.ListRepository
import com.benmohammad.postapp.list.viewmodel.ListViewModelFactory
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit

@ListScope
@Component(dependencies = [CoreComponent::class], modules = [ListModule::class])
interface ListComponent {

    fun postDb(): PostDb

    fun postService(): PostService

    fun picasso(): Picasso

    fun scheduler(): Scheduler

    fun inject(listActivity: ListActivity)


}

@Module
class ListModule {
    @Provides
    @ListScope
    fun adapter(picasso: Picasso): PostListAdapter = PostListAdapter(picasso)

    @Provides
    @ListScope
    fun listViewModelFactory(repository: ListDataContract.Repository, compositeDisposable: CompositeDisposable): ListViewModelFactory = ListViewModelFactory(repository, compositeDisposable)

    @Provides
    @ListScope
    fun listRepo(local: ListDataContract.Local, remote: ListDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable): ListDataContract.Repository = ListRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    @ListScope
    fun remoteData(postService: PostService): ListDataContract.Remote = ListRemoteData(postService)

    @Provides
    @ListScope
    fun localData(postDb: PostDb, scheduler: Scheduler): ListDataContract.Local = ListLocalData(postDb, scheduler)

    @Provides
    @ListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @ListScope
    fun postDb(context: Context): PostDb = Room.databaseBuilder(context, PostDb::class.java, Constants.Posts.DB_NAME).build()

    @Provides
    @ListScope
    fun postService(retrofit: Retrofit): PostService = retrofit.create(PostService::class.java)
}