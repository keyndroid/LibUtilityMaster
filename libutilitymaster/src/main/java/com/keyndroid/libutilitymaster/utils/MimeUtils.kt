package com.keyndroid.libutilitymaster.utils

object MimeUtils {

    public fun getName(filename: String): String {
        return filename.substring(
            filename.lastIndexOf('/') + 1,
            filename.length
        )
    }

    /**
     * Determines the MIME type for a given filename.
     *
     * @param filename
     * The file to determine the MIME type of.
     * @return The MIME type of the file, or a wildcard if none could be
     * determined.
     */
    fun getType(filename: String): String {
        // There does not seem to be a way to ask the OS or file itself for this
        // information, so unfortunately resorting to extension sniffing.

        val pos = filename.lastIndexOf('.')
        if (pos != -1) {
            val ext = filename.substring(
                filename.lastIndexOf('.') + 1,
                filename.length
            )

            if (ext.equals("mp3", ignoreCase = true))
                return "audio/mpeg"
            if (ext.equals("aac", ignoreCase = true))
                return "audio/aac"
            if (ext.equals("wav", ignoreCase = true))
                return "audio/wav"
            if (ext.equals("ogg", ignoreCase = true))
                return "audio/ogg"
            if (ext.equals("mid", ignoreCase = true))
                return "audio/midi"
            if (ext.equals("midi", ignoreCase = true))
                return "audio/midi"
            if (ext.equals("wma", ignoreCase = true))
                return "audio/x-ms-wma"

            if (ext.equals("mp4", ignoreCase = true))
                return "video/mp4"
            if (ext.equals("avi", ignoreCase = true))
                return "video/x-msvideo"
            if (ext.equals("wmv", ignoreCase = true))
                return "video/x-ms-wmv"

            if (ext.equals("png", ignoreCase = true))
                return "image/png"
            if (ext.equals("jpg", ignoreCase = true))
                return "image/jpeg"
            if (ext.equals("jpe", ignoreCase = true))
                return "image/jpeg"
            if (ext.equals("jpeg", ignoreCase = true))
                return "image/jpeg"
            if (ext.equals("gif", ignoreCase = true))
                return "image/gif"

            if (ext.equals("xml", ignoreCase = true))
                return "text/xml"
            if (ext.equals("txt", ignoreCase = true))
                return "text/plain"
            if (ext.equals("cfg", ignoreCase = true))
                return "text/plain"
            if (ext.equals("csv", ignoreCase = true))
                return "text/plain"
            if (ext.equals("conf", ignoreCase = true))
                return "text/plain"
            if (ext.equals("rc", ignoreCase = true))
                return "text/plain"
            if (ext.equals("htm", ignoreCase = true))
                return "text/html"
            if (ext.equals("html", ignoreCase = true))
                return "text/html"

            if (ext.equals("pdf", ignoreCase = true))
                return "application/pdf"
            if (ext.equals("apk", ignoreCase = true))
                return "application/vnd.android.package-archive"

            // Additions and corrections are welcomed.
        }
        return "*/*"
    }

}
