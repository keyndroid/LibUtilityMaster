# LibUtilityMaster for Kotlin

[![Release](https://jitpack.io/v/jitpack/android-example.svg)](https://jitpack.io/#keyndroid/LibUtilityMaster)

LibUtilityMaster library that works with jitpack.io.

Add it to your project level build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

and to your app level build.gradle:

```gradle
dependencies {
    implementation 'com.github.keyndroid:LibUtilityMaster:{latest version}'
}
```

# Permission Utility

First you need to add permission in your AndroidManifest.xml

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

Also you need to add this activity in your AndroidManifest.xml

```
<activity android:name="com.demo.libutility.utility.permission.PermissionActivity"
          android:theme="@style/Theme.Transparent"
          />
```

You can use this code for asking permission

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

Here you can also add multiple permissions simultaneously

```
permissionBuilder.addPermission(
                    EnumHelper.Permission.CAMERA,
                    EnumHelper.Permission.STORAGE
                )
```


# FilePicker Utility

you need to add this activity in your AndroidManifest.xml

```
<activity android:name="com.demo.libutility.utility.gallery.FilePickerActivity"
          android:theme="@style/Theme.Transparent"
          />
```

Now you need to open file picker dialog for according to your requirements.
Here I have added 3 
```
 val fileBuilder = FileHelper.Builder(this)
                .addFilerPicker(EnumHelper.FilePicker.CAMERA)
                .addFilerPicker(EnumHelper.FilePicker.GALLERY)
                .addFilerPicker(EnumHelper.FilePicker.FILE)
                .addFileMimeType(MimeUtils.getType(".pdf"))
                .setCallbackPermission(object : CallbackFileSelection {


                    override fun onFileSelectionFile(fileData: FileData) {
                       
                    }

                    override fun onFileSelectionError(message: String) {

                    }
                })
```


Incaase you want to open multiple Images from gallery, you can add this extra line (This is optional, by default 1 Image will be selected)

```
fileBuilder.addMaxImage(countBalue)
```

At the end you can build file picker

```
fileBuilder.build()
```

when you adding file picker you need to pass MIME type for it. You can add any MIME type i.e.   gif,pdf,apk,mp3,aac,midi,mp4,png,jpg etc.

```
 fileBuilder.addFilerPicker(EnumHelper.FilePicker.FILE)
            .addFileMimeType(MimeUtils.getType(".pdf")) 
```






