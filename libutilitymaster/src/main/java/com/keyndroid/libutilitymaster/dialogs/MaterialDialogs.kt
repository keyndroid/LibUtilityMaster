package com.keyndroid.libutilitymaster.dialogs

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keyndroid.libutilitymaster.utils.EnumHelper

/**
 * Class to create material dialog theme wise
 * @param style add style(nullable) from
 * @see EnumHelper.STYLE
 *
 */
class MaterialDialogs(val style: EnumHelper.STYLE?) {
    lateinit var alertDialog: MaterialAlertDialogBuilder
    lateinit var onClickListener: DialogCallback
    var selectedItem: Int = 0
    var selectedItemList: MutableList<Int> = ArrayList()

    interface DialogCallback {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    val ClickListener = object : DialogInterface.OnClickListener {
        override fun onClick(view: DialogInterface?, what: Int) {
            if (!::onClickListener.isInitialized) {
                view?.dismiss()
                return
            }
            if (what == DialogInterface.BUTTON_POSITIVE) {
                onClickListener.onPositiveClick()
            } else if (what == DialogInterface.BUTTON_NEGATIVE || what == DialogInterface.BUTTON_NEUTRAL) {
                onClickListener.onNegativeClick()
            } else {
                selectedItem = what
            }
        }
    }
    val multiCLickListener = object : DialogInterface.OnMultiChoiceClickListener {
        override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean) {

        }
    }

    fun create(
        context: Context,
        message: String,
        positiveText: String,
        negativeText: String
    ): MaterialAlertDialogBuilder {

        alertDialog = MaterialAlertDialogBuilder(context)

        if (style != null) {
            alertDialog = MaterialAlertDialogBuilder(context, style.theme)
        }
        alertDialog.setMessage(message)
            .setPositiveButton(positiveText, ClickListener)
            .setNegativeButton(negativeText, ClickListener)

        return alertDialog
    }

    fun setCallback(onClickListener: DialogCallback): MaterialDialogs {
        this.onClickListener = onClickListener
        return this
    }

    fun create(
        context: Context,
        message: String,
        layoutResId: Int,
        positiveText: String,
        negativeText: String
    ): MaterialAlertDialogBuilder {

        alertDialog = MaterialAlertDialogBuilder(context)
        if (style != null) {
            alertDialog = MaterialAlertDialogBuilder(context, style.theme)
        }
        alertDialog.setMessage(message)
            .setView(layoutResId)
            .setPositiveButton(positiveText, ClickListener)
            .setNegativeButton(negativeText, ClickListener)
        return alertDialog
    }

    fun create(
        context: Context,
        message: String,
        choices: Array<String>,
        positiveText: String,
        neutralText: String,
        choicesInitial: BooleanArray?
    ): MaterialAlertDialogBuilder {
        alertDialog = MaterialAlertDialogBuilder(context)
        if (style != null) {
            alertDialog = MaterialAlertDialogBuilder(context, style.theme)
        }
        alertDialog.setMessage(message)
            .setPositiveButton(positiveText, ClickListener)
            .setNeutralButton(neutralText, ClickListener)
            .setMultiChoiceItems(choices, choicesInitial, multiCLickListener)
        return alertDialog
    }

    /**
     *
     */
    fun create(
        context: Context,
        title: String,
        choices: Array<String>,
        positiveText: String,
        neutralText: String,
        choicesInitial: Int
    ): MaterialAlertDialogBuilder {
        alertDialog = MaterialAlertDialogBuilder(context)
        if (style != null) {
            alertDialog = MaterialAlertDialogBuilder(context, style.theme)
        }
        selectedItem = choicesInitial
        alertDialog
            .setTitle(title)
            .setPositiveButton(positiveText, ClickListener)
            .setNeutralButton(neutralText, ClickListener)
            .setSingleChoiceItems(choices, choicesInitial, ClickListener)
        return alertDialog
    }

}
