package com.keyndroid.libutilitymaster.utils

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.util.TypedValue
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu

/**
 * @author Keyur
 * This class is used to show new material style views
 */
public class MaterialUtility {
    /**
     * Method to show popoup
     * @param context pass context of activity or fragment
     * @param view pass on which view you want to open popup
     * @param menuRes pass menu resource
     * @return PopupMenu so that can set click listener
     */
    public fun showMenu(context: Context, view: View, @MenuRes menuRes: Int): PopupMenu {
        val popup = PopupMenu(context, view)
        // Inflating the Popup using xml file
        popup.menuInflater.inflate(menuRes, popup.menu)
        // There is no public API to make icons show on menus.
        // IF you need the icons to show this works however it's discouraged to rely on library only
        // APIs since they might disappear in future versions.
        if (popup.menu is MenuBuilder) {
            val menuBuilder:MenuBuilder = popup.menu as MenuBuilder

            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ICON_MARGIN.toFloat(),
                    context.getResources().getDisplayMetrics()
                ).toInt()

                if (item.icon != null) {
                    item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                }
            }
        }
        popup.setOnMenuItemClickListener { menuItem ->

            true
        }
        return popup
    }
    private val ICON_MARGIN = 8

}