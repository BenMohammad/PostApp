package com.benmohammad.core.testing

import androidx.annotation.VisibleForTesting
import com.benmohammad.core.networking.Scheduler
import io.reactivex.schedulers.Schedulers

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
class TestScheduler: Scheduler {

    override fun mainThread(): io.reactivex.Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): io.reactivex.Scheduler {
        return Schedulers.trampoline()
    }
}