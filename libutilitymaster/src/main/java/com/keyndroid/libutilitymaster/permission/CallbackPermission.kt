package com.keyndroid.libutilitymaster.permission

interface CallbackPermission {
    fun onPermissionGranted()
    fun onPermissionDenied()
}