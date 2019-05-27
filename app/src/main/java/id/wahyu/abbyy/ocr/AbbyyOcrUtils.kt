package id.wahyu.abbyy.ocr

import android.app.Activity
import android.util.Log
import com.abbyy.mobile.rtr.Engine
import com.abbyy.mobile.rtr.IRecognitionService
import com.abbyy.mobile.rtr.ITextCaptureService
import com.abbyy.mobile.rtr.ITextCaptureService.TextLine
import com.abbyy.mobile.rtr.Language
import com.example.ocrapp.BuildConfig
import com.example.ocrapp.R

class AbbyyOcrUtils(activity: Activity) {

    // Licensing
    private val licenseFileName = "MCTT-0100-0006-6681-3414-3767.ABBYY.License"

    private val languages = arrayOf(
        Language.Indonesian
    )

    // The 'Abbyy RTR SDK Engine' and 'Text Capture Service' to be used in this sample application
    private var engine: Engine? = null
    private var textCaptureService: ITextCaptureService? = null
    private var mActivity: Activity

    init {
        createTextCaptureService(activity)
        mActivity = activity
    }

    // Load ABBYY RTR SDK engine and configure the text capture service
    private fun createTextCaptureService(activity: Activity): Boolean {
        // Initialize the engine and text capture service
        try {
            engine = Engine.load(activity, licenseFileName)
            textCaptureService = engine!!.createTextCaptureService(object : ITextCaptureService.Callback {

                override fun onRequestLatestFrame(buffer: ByteArray) {}

                override fun onFrameProcessed(
                    p0: Array<out TextLine>?,
                    p1: IRecognitionService.ResultStabilityStatus?,
                    p2: IRecognitionService.Warning?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


                override fun onError(e: Exception) {
                    // An error occurred while processing. Log it. Processing will continue
                    if (BuildConfig.DEBUG) {
                        // Make the error easily visible to the developer
                        var message: String? = e.message
                        if (message == null) {
                            message = "Unspecified error while creating the service. See logcat for details."
                        } else {
                            if (message.contains("ChineseJapanese.rom")) {
                                message =
                                    "Chinese, Japanese and Korean are available in EXTENDED version only. Contact us for more information."
                            }
                            if (message.contains("Russian.edc")) {
                                message =
                                    "Cyrillic script languages are available in EXTENDED version only. Contact us for more information."
                            } else if (message.contains(".trdic")) {
                                message =
                                    "Translation is available in EXTENDED version only. Contact us for more information."
                            }
                        }
                    }
                }
            })

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
    private fun startRecognition() {
        // Start the service
       // textCaptureService.start(cameraPreviewSize.width, cameraPreviewSize.height, orientation, areaOfInterest)

    }

    // Stop recognition
    internal fun stopRecognition() {

    }

}