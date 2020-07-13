package com.benmohammad.postapp.details.model

import com.benmohammad.core.extensions.*
import com.benmohammad.core.networking.Outcome
import com.benmohammad.core.networking.synk.Synk
import com.benmohammad.core.networking.synk.SynkKeys
import com.benmohammad.postapp.commons.data.local.Comment
import com.benmohammad.postapp.details.exceptions.DetailsExceptions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class DetailsRepository(
    private val local: DetailsDataContract.Local,
    private val remote: DetailsDataContract.Remote,
    private val scheduler: com.benmohammad.core.networking.Scheduler,
    private val compositeDisposable: CompositeDisposable
): DetailsDataContract.Repository {

    override val commentsFetchOutcome: PublishSubject<Outcome<List<Comment>>> =
        PublishSubject.create<Outcome<List<Comment>>>()

    override fun fetchCommentsFor(postId: Int?) {
        if(postId == null)
            return

        commentsFetchOutcome.loading(true)
        local.getCommentsForPost(postId)
            .performOnBackOutOnMain(scheduler)
            .doAfterNext{
                if(Synk.shouldSync(SynkKeys.POST_DETAILS + " " + postId, 2, TimeUnit.HOURS))
                    refreshComments(postId)
            }
            .subscribe({comments ->
                commentsFetchOutcome.success(comments)
            }, {error -> handleError(error)}

            ).addTo(compositeDisposable)
    }

    override fun refreshComments(postId: Int) {
        commentsFetchOutcome.loading(true)
        remote.getCommentsForPost(postId)
            .performOnBackOutOnMain(scheduler)
            .subscribe(
                {comments -> saveCommentsForPost(comments)},
                {error -> handleError(error)}
            ).addTo(compositeDisposable)
    }

    override fun saveCommentsForPost(comments: List<Comment>) {
        if(comments.isNotEmpty()) {
            local.saveComments(comments)
        } else {
            commentsFetchOutcome.failed(DetailsExceptions.NoComments())
        }
    }

    override fun handleError(error: Throwable) {
        commentsFetchOutcome.failed(error)
    }
}