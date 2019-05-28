package id.wahyu.abbyy.ocr

import android.app.Activity
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.abbyy.mobile.rtr.Engine
import com.abbyy.mobile.rtr.IRecognitionCoreAPI
import com.abbyy.mobile.rtr.IRecognitionCoreAPI.TextRecognitionCallback
import com.abbyy.mobile.rtr.Language
import com.example.ocrapp.R

class AbbyyOcrUtils(activity: Activity) : TextRecognitionCallback {
    override fun onTextOrientationDetected(p0: Int) {

    }

    override fun onProgress(p0: Int, p1: IRecognitionCoreAPI.Warning?): Boolean {
        return true
    }

    override fun onError(p0: Exception?) {

    }

    // Licensing
    private val licenseFileName = "MCTT-0100-0006-6681-3414-3767.ABBYY.License"

    private val languages = arrayOf(
        Language.Indonesian
    )

    // The 'Abbyy RTR SDK Engine' and 'Text Capture Service' to be used in this sample application
    private var engine: Engine? = null
    private var mActivity: Activity
    private var recognitionAPI : IRecognitionCoreAPI? = null

    init {
        createTextCaptureService(activity)
        mActivity = activity
    }

    // Load ABBYY RTR SDK engine and configure the text capture service
    private fun createTextCaptureService(activity: Activity): Boolean {
        // Initialize the engine and text capture service
        try {
            engine = Engine.load(activity, licenseFileName)
            recognitionAPI = engine!!.createRecognitionCoreAPI()

            return true
        } catch (e: java.io.IOException) {
            // Troubleshooting for the developer
            Log.e(activity.getString(R.string.app_name), "Error loading ABBYY RTR SDK:", e)
        } catch (e: Engine.LicenseException) {
            // Troubleshooting for the developer
            Log.e(activity.getString(R.string.app_name), "Error loading ABBYY RTR SDK:", e)
        } catch (e: Throwable) {
            // Troubleshooting for the developer
            Log.e(activity.getString(R.string.app_name), "Error loading ABBYY RTR SDK:", e)
        }

        return false
    }

    // Start recognition
    internal fun startRecognition(bitmap: Bitmap){
        RecognitionTask().execute(RecognitionData(bitmap,recognitionAPI!!,this))
    }

    // Stop recognition
    internal fun stopRecognition() {
        recognitionAPI!!.close()
    }

    data class RecognitionData(
        val image: Bitmap,
        val recognitionApi: IRecognitionCoreAPI,
        val callback: TextRecognitionCallback
    )

    class RecognitionTask : AsyncTask<RecognitionData, String, String>() {

        companion object {
            private val TAG = RecognitionTask::class.java.simpleName
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d(TAG, "onPreExecute: " )
        }

        override fun doInBackground(vararg data: RecognitionData): String {
            val recognitionApi = data[0].recognitionApi
            val image = data[0].image
            val callback = data[0].callback
            var stringBuffer = StringBuffer()
            for(textBlock : IRecognitionCoreAPI.TextBlock? in recognitionApi.recognizeText(image,callback)){
               for(textLine : IRecognitionCoreAPI.TextLine? in textBlock!!.TextLines){
                   stringBuffer.append(textLine!!.Text + "\n")
               }
            }
            return stringBuffer.toString()
        }

        override fun onPostExecute(s: String) {
            Log.d("ABBYY OCR RESULT", s)
        }
    }
}