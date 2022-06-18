package com.revolve44.solarpanelx.domain.base.recyclerview

import android.view.View
import com.revolve44.solarpanelx.ui.models.StoriesLikeCardsInformation


interface ItemElementsDelegate<T> {
    fun onElementClick(model: T, view: View, clickedPosition: Int)
    fun onItemClick(item: StoriesLikeCardsInformation)

}