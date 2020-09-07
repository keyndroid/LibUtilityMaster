package com.keyndroid.libutilitymaster.utils

import android.Manifest
import android.annotation.SuppressLint
import com.keyndroid.libutilitymaster.R

/**
 *Created by Keyur on 20,September,2019
 * THis class is created for using all enum required for this library
 * @see SwipeDirection
 * @see STYLE
 * @see Permission
 * @see FilePicker
 */
public class EnumHelper {

    /**
     * Enum for swipe direction viewpager
     * @param all
     *
     */
    public enum class SwipeDirection {
        /**
         * Allow swipe in all direction
         */
        all,
        /**
         * Allow swipe left direction
         */
        left,
        /**
         * Allow swipe right direction
         */
        right,
        /**
         * Restrict Swipe direction in any direction
         */
        none
    }

    /**
     * Enum style for Material dialog button style
     */
    public enum class STYLE(val theme: Int) {
        /**
         * If button Filled then choose this style
         */
        Filled(R.style.ThemeOverlay_Catalog_MaterialAlertDialog_FilledButton),
        /**
         * If button outlined require then use this style
         */
        Outlined(R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton)
    }

    /**
     * Enum for permission
     *
     */
    public enum class Permission {
        /**
         * If only CAMERA need to open then select this permission
         */
        CAMERA {
            override fun getPermisions() = arrayOf(Manifest.permission.CAMERA)
        },
        /**
         * If need to open storage then select this permission
         */
        STORAGE {
            override fun getPermisions() = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        },
        /**
         * This enum is useful when need to show picker with camera and storage both
         */
        IMAGE_PICKER {
            override fun getPermisions(): Array<String> {
                return CAMERA.getPermisions().plus(STORAGE.getPermisions())
            }
        },
        /**
         * This enum is for location permission
         */
        LOCATION {
            override fun getPermisions() = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        },
        /**
         * This enum is only for api level 29 or above
         * Bacdkground location permission
         */
        BACKGROUND_LOCATION {
            //Only for api level 29 or above
            @SuppressLint("InlinedApi")
            override fun getPermisions() =
                LOCATION.getPermisions().plus(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            //PermisssionUtils.Permission.LOCATION.getPermisions()
//                ACCESS_BACKGROUND_LOCATION
        };

        abstract fun getPermisions(): Array<String>

    }

    /**
     * File picker enum is use which type of files require to open
     */
    public enum class FilePicker {
        /**
         * Only camera can be open
         */
        CAMERA {
            override fun getPicker(): String {
                return "Camera"
            }

        },
        /**
         * Storage will be open with only image selection
         */
        GALLERY {
            override fun getPicker(): String {
                return "Gallery"
            }
        },
        /**
         * Storage will be open with specified file mime type
         */
        FILE {
            override fun getPicker(): String {
                return "File"
            }
        };

        abstract fun getPicker(): String
    }
}