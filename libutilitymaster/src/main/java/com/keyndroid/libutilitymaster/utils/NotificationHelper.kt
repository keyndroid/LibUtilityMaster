package com.keyndroid.libutilitymaster.utils

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.keyndroid.libutilitymaster.R

class NotificationHelper(var context: Context){

    init{
        //This is for static purpose
        for (i in 0..1){
            var channgelGroup = getChannelGroup(i)
            createGroup(channgelGroup,"ChannelGroup is created=$channgelGroup")
        }
        for (i in 0..5){
            var channgelGroup = getGroupIdFromChannelId(i)
            createChannel(i%2==0,getChannelId(i),channgelGroup,"ChanneName is described as $i",
            "Channel Description = This is channel $i. Please dont hide this notificaiton.")
        }
        Log.e("NotificationData","===============================================================")
        Log.e("NotificationData","===============================================================")
        Log.e("NotificationData","===============================================================")
        Log.e("NotificationData","===============================================================")
    }



    fun createNOtification(isGroup:Boolean,pos:Int,intent:Intent){
        val CHANNEL_ID=getChannelId(pos)
        /*var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)*/

        var builder:NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = NotificationCompat.Builder(context,CHANNEL_ID)
        }
        else{
            builder = NotificationCompat.Builder(context)
        }
        val inboxStyle = NotificationCompat.InboxStyle()

        val groupId = getGroupIdFromChannelId(pos)
        Log.e("NotificationData","CHANNEL_ID==$CHANNEL_ID==GroupID==$groupId")
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
            .setContentTitle("Channel ID= $CHANNEL_ID==GroupId=$groupId")

            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(inboxStyle)
            .setAutoCancel(true)
//            .setGroupSummary(true)
//            .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line...\"Much longer text that cannot fit one line...\"\"Much longer text that cannot fit one line...\""))
//            .setChannelId(CHANNEL_ID)
//            .setGroup("com.android.example.WORK_EMAIL")

//        builder.setStyle(inboxStyle)


        // Create an explicit intent for an Activity in your app
//        val intent = Intent(context, redirectionClass).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//            .setChannelId(CHANNEL_ID)
//            .setNumber(19)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)


        createGroup(groupId,"Group is $groupId")
        createChannel(notificationId%2==0,CHANNEL_ID,groupId,CHANNEL_ID,"CHaanelDescription$CHANNEL_ID")
            // Set the intent that will fire when the user taps the notification
           builder.setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
            notificationId++
        }
    }

    fun getChannelGroup(pos:Int): String {
        val groupId= notificationId%2
        var channgelGroup = "Group_$pos"
        return channgelGroup
    }
    fun getGroupIdFromChannelId(pos:Int): String {
        return getChannelGroup(pos%2)
    }
    fun getChannelId(pos:Int): String {
        return "ChannelId$pos"
    }

    fun createGroup(channelGroup: String, groupDescription:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationGroup = NotificationChannelGroup(
                channelGroup,
                groupDescription.plus("Name")
            )
            notificationManager.createNotificationChannelGroup(notificationGroup)

        }
    }
    fun createChannel(isGroupAdd:Boolean, CHANNEL_ID:String, channelGroup:String, channelName:String, channelDescription:String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationGroup = NotificationChannelGroup(channelGroup,
                CHANNEL_ID.plus(channelGroup).plus("Name"))
//            notificationManager.createNotificationChannelGroup(notificationGroup)


            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDescription
                setShowBadge(true)
            }
            if (isGroupAdd){
                channel.group=notificationGroup.id
            }

            channel.setShowBadge(true)
            // Register the channel with the system

            Log.e("NotificationData","CHANNEL_ID==$CHANNEL_ID==GroupID==${notificationGroup.id}")
            notificationManager.createNotificationChannel(channel)
        }
    }
    companion object{
        var notificationId=1
    }
}
