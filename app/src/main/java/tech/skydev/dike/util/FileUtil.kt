package tech.skydev.dike.util

import android.content.Context

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * Created by Hash Skyd on 3/24/2017.
 */

object FileUtil {
    private val DEFAULT_BUFFER_SIZE = 1024 * 4
    /**
     * Returns the text of a file as a String object

     * @param context  The current context
     * *
     * @param fileName The name of the file to load from assets folder
     * *
     * @return The text content of the file
     */
    @Throws(IOException::class)
    fun loadTextFileFromAssets(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        try {
            val outputStream = ByteArrayOutputStream()
            try {
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var n: Int
                n = inputStream.read(buffer)
                while (n >= 0) {
                    outputStream.write(buffer, 0, n);
                    n = inputStream.read(buffer)
                }
                return outputStream.toString()
            } finally {
                outputStream.close()
            }
        } finally {
            inputStream.close()
        }
    }
}
