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
    companion object {
        private val TAG = AbbyyOcrUtils::class.java.simpleName
    }


    override fun onTextOrientationDetected(p0: Int) {
        Log.d(TAG, "" + p0)
    }

    override fun onProgress(p0: Int, p1: IRecognitionCoreAPI.Warning?): Boolean {
        return true
    }

    override fun onError(p0: Exception?) {
        Log.d(TAG, "" + p0)
    }

    // Licensing
    private val licenseFileName = "MCTT-0100-0006-6681-3414-3767.ABBYY.License"

    private val languages = arrayOf(
        Language.Indonesian
    )

    // The 'Abbyy RTR SDK Engine' to be used in this sample application
    private var engine: Engine? = null
    private var mActivity: Activity
    private var recognitionAPI: IRecognitionCoreAPI? = null

    init {
        createEngine(activity)
        mActivity = activity
    }

    // Load ABBYY RTR SDK engine
    private fun createEngine(activity: Activity) {
        // Initialize the engine
        try {
            engine = Engine.load(activity, licenseFileName)
            recognitionAPI = engine!!.createRecognitionCoreAPI()
            recognitionAPI!!.textRecognitionSettings.setRecognitionLanguage(Language.English)
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
    }

    // Start recognition
    internal fun startRecognition(bitmap: Bitmap, callback : RecognitionTask.RecognitionTaskListener) {

        RecognitionTask(callback).execute(RecognitionData(bitmap, recognitionAPI!!, this))
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

    class RecognitionTask(val listener : RecognitionTaskListener) : AsyncTask<RecognitionData, String, String>() {
        companion object {
            private val TAG = RecognitionTask::class.java.simpleName
        }

        interface RecognitionTaskListener {
            // callback for start
            fun onStarted()

            // callback on success
            fun onCompleted(result : String?)

            // callback on error
            fun onError(errorMessage: String?)

        }

        override fun onPreExecute() {
            super.onPreExecute()
            listener.onStarted()
            Log.d(TAG, "onPreExecute: ")
        }

        override fun doInBackground(vararg data: RecognitionData): String {
            val recognitionApi = data[0].recognitionApi
            val image = data[0].image
            val callback = data[0].callback
            val stringBuffer = StringBuffer()
            var textBlocks  = recognitionApi.recognizeText(image, callback)
            if (textBlocks != null) {
                try {
                    for (textBlock: IRecognitionCoreAPI.TextBlock? in textBlocks) {
                        for (textLine: IRecognitionCoreAPI.TextLine? in textBlock!!.TextLines) {
                            stringBuffer.append(textLine!!.Text + "\n")
                        }
                    }
                    return stringBuffer.toString()
                }catch (e : java.lang.Exception){
                    return "Error : " + e.message
                }

            }
            else
                return "Error : " + "No text blocks found"
        }

        override fun onPostExecute(result: String) {
            if(result.contains("Error")){
                listener.onError(result)
            }else{
                listener.onCompleted(result)
            }
        }
    }
}