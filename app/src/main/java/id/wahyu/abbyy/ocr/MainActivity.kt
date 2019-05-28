package id.wahyu.abbyy.ocr

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.ocrapp.R
import id.wahyu.abbyy.ocr.BitmapUtils.BitmapUtils.getBitmapFromAsset
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity :  AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var mSelectedImage: Bitmap? = null
    private var abbyyOcrUtils : AbbyyOcrUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        abbyyOcrUtils = AbbyyOcrUtils(this@MainActivity)
        button.setOnClickListener{ startPrediction()}
        val items = arrayOf(
            "KTP 1",
            "KTP 2",
            "KTP 3")
        val adapter = ArrayAdapter(this, android.R.layout
            .simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

    }


    override fun onItemSelected(parent: AdapterView<*>, v: View, position: Int, id: Long) {
        mSelectedImage = null
        when (position) {
            0 -> mSelectedImage = getBitmapFromAsset(this, "ktp_1.jpg")
            1 -> mSelectedImage = getBitmapFromAsset(this, "ktp_2.jpg")
            2 -> mSelectedImage = getBitmapFromAsset(this, "ktp_3.jpg")
        }
        image_view!!.setImageBitmap(mSelectedImage)

    }

    private fun startPrediction(){
        val croppedImage = BitmapUtils.BitmapUtils.cropImage(mSelectedImage!!, 425, 40, 210, 100)
        image_view2!!.setImageBitmap(croppedImage)
        abbyyOcrUtils!!.startRecognition(mSelectedImage!!, object : AbbyyOcrUtils.RecognitionTask.RecognitionTaskListener{
            override fun onCompleted(result: String?) {
                progressbar.visibility = View.GONE
                text_view.text = result
            }

            override fun onError(errorMessage: String?) {
                progressbar.visibility = View.GONE
                text_view.text = errorMessage
            }

            override fun onStarted() {
                progressbar.visibility = View.VISIBLE
                text_view.text = ""
            }
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Do nothing
    }

    override fun onStop() {
        abbyyOcrUtils!!.stopRecognition()
        super.onStop()
    }


}
