package com.keyndroid.libutilitymaster.permission

import android.app.Activity
import com.keyndroid.libutilitymaster.utils.EnumHelper

public class PermissionUtils(builder: Builder) {
    var context:Activity
    var permissionList: MutableList<String>
    init {
        context=builder.context
        permissionList=builder.permissionList
        askPermission()
    }


    private fun askPermission(){
        PermissionActivity.newIntent(context,permissionList.toTypedArray())
    }



    public class Builder {
        lateinit var context: Activity
            private set
        var permissionList: MutableList<String> = mutableListOf()
            private set

        fun setContenxt(context: Activity): Builder {
            this.context=context
            return this
        }

        fun setCallbackPermission(callbackPermission: CallbackPermission): Builder {
            PermissionActivity.callbackPermission=callbackPermission
            return this
        }

        fun addPermission(vararg permissions : EnumHelper.Permission): Builder {
//            this.permission = permission
            for (permission in permissions){
                permissionList.addAll(permission.getPermisions())
            }
//            permissionList = permission.getPermisions()
            return this
        }

        fun build(): PermissionUtils {
            if (!::context.isInitialized){
                throw IllegalStateException("Context must not be null")
            }
            else if (permissionList.size==0){
                throw IllegalStateException("Atleast 1 Permission should be passed")
            }
            else if (PermissionActivity.callbackPermission==null){
                throw IllegalStateException("Callback not found")
            }
//            val list = permissionList.distinct()
            permissionList= permissionList.distinct().toMutableList()
            return PermissionUtils(this)
        }
    }

}
