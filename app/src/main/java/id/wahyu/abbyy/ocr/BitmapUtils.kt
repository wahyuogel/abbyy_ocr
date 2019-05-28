package id.wahyu.abbyy.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import java.io.IOException
import java.io.InputStream


class BitmapUtils {
    /**
     * Get list of Character from Image Path based on specific rectangle region and position.
     */

    object BitmapUtils {
        /**
         * Crop Image from bitmap
         */
        fun cropImage(bm: Bitmap, rectWidth: Int, rectHeight: Int, x: Int, y: Int): Bitmap {
            val rect = Rect(0, 0, rectWidth, rectHeight)
            val mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true)
            try {
                return Bitmap.createBitmap(mutableBitmap, x, y, rect.width(), rect.height())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bm
        }

        fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
            val assetManager = context.assets
            val `is`: InputStream
            var bitmap: Bitmap? = null
            try {
                `is` = assetManager.open(filePath)
                bitmap = BitmapFactory.decodeStream(`is`)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bitmap
        }
    }
}