package com.benmohammad.postapp.commons

import com.benmohammad.core.application.CoreApp
import com.benmohammad.postapp.details.di.DaggerDetailsComponent
import com.benmohammad.postapp.details.di.DetailsComponent
import com.benmohammad.postapp.list.di.DaggerListComponent
import com.benmohammad.postapp.list.di.ListComponent
import javax.inject.Singleton

@Singleton
object PostDH {

    private var listComponent: ListComponent? = null
    private var detailsComponent: DetailsComponent? = null

    fun listComponent(): ListComponent {
        if(listComponent == null)
            listComponent = DaggerListComponent.builder().coreComponent(CoreApp.coreComponent).build()
        return listComponent as ListComponent
    }

    fun destroyListComponent() {
        listComponent = null
    }

    fun detailsComponent(): DetailsComponent {
        if(detailsComponent == null)
            detailsComponent = DaggerDetailsComponent.builder().listComponent(listComponent).build()
        return detailsComponent as DetailsComponent
    }

    fun destroyDetailsComponent() {
        detailsComponent = null
    }
}