package com.revolve44.solarpanelx.ui.models

import android.graphics.drawable.Drawable

data class ToolsRecyclerviewModel(
    val name : String,
    val status : String,
    val notifications : Int,
    val iconOfTool : Drawable?
)
