package com.revolve44.solarpanelx.global_utils

import android.R.id.message
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.revolve44.solarpanelx.R


fun toastShow(msg: String, colorText : Int , ctx : Context) {

    var view: View = LayoutInflater.from(ctx)
        .inflate(R.layout.toast_layout, null)
    var toast = Toast(ctx)
    var tvMessage = view.findViewById<TextView>(R.id.toast_msg)

    tvMessage.setText(msg)
    tvMessage.setTextColor(colorText)
    toast.duration = Toast.LENGTH_LONG
    toast.setView(view);
    toast.show();


}