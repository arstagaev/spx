package com.revolve44.solarpanelx.domain.base.materialdialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class BaseMaterialDialogFragment(viewID : Int) : DialogFragment(viewID) {

    private var dialogView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme).apply {
            dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)

            dialogView?.let { onViewCreated(it, savedInstanceState) }
            setView(dialogView)
        }.create()
    }

    override fun getView(): View? {
        return dialogView
    }
}