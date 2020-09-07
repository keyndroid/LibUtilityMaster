# LibUtilityMaster

[![Release](https://jitpack.io/v/jitpack/android-example.svg)](https://jitpack.io/#keyndroid/LibUtilityMaster)

LibUtilityMaster library that works with jitpack.io.

Add it to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

and:

```gradle
dependencies {
    implementation 'com.github.keyndroid:LibUtilityMaster:{latest version}'
}
```

# Permission Utility

You can use this code for permission

```
val permissionBuilder = PermissionUtils.Builder().setContenxt(this)
permissionBuilder.addPermission(EnumHelper.Permission.STORAGE)
 permissionBuilder.setCallbackPermission(object : CallbackPermission {
            override fun onPermissionDenied() {
               
            }

            override fun onPermissionGranted() {
               
            }
        })
        .build()
```
