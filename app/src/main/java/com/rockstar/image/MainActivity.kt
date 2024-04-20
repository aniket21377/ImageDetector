package com.rockstar.image

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.core.view.drawToBitmap
import android.widget.ImageView
import com.rockstar.image.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("image")
        }
    }
    external fun helloWorld(): String
//    external fun helloWorld()
    lateinit var choose:Button
    lateinit var predict:Button
    lateinit var resview:TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        choose=findViewById(R.id.button)
        predict=findViewById(R.id.button2)
        resview=findViewById(R.id.textView)
        imageView=findViewById(R.id.imageView)
        resview.text = helloWorld()
//        helloWorld()
        var labels=application.assets.open("labels.txt").bufferedReader().readLines()
        var imageprocessor=ImageProcessor.Builder()
//            .add(NormalizeOp(0.0f,255.0f))
//            .add(TransformToGrayscaleOp())
            .add(ResizeOp(224,224,ResizeOp.ResizeMethod.BILINEAR))
            .build()

        choose.setOnClickListener {
            var intent:Intent=Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,100)
        }
        predict.setOnClickListener {
            var tensorImage=TensorImage(DataType.UINT8)
            tensorImage.load(bitmap)
            tensorImage=imageprocessor.process(tensorImage)

            val model = MobilenetV110224Quant.newInstance(this)

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            inputFeature0.loadBuffer(tensorImage.buffer)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
            var maxIdx=0;
            outputFeature0.forEachIndexed { index, fl ->
                if(outputFeature0[maxIdx]<fl){
                    maxIdx=index;
                }
            }
            resview.setText(labels[maxIdx])

// Releases model resources if no longer used.
            model.close()
        }
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode==100){
//            var uri=data?.data;
//            bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
//            imageView.setImageBitmap(bitmap)
//        }
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(inputStream, null, options)
                inputStream?.close()

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, 224, 224)

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false
                val inputStreamSecond = contentResolver.openInputStream(uri)
                var resizedBitmap = BitmapFactory.decodeStream(inputStreamSecond, null, options)
                inputStreamSecond?.close()

                // Set the resized bitmap to your ImageView
                imageView.setImageBitmap(resizedBitmap)

                // Assign the resized bitmap to your global bitmap variable
                if (resizedBitmap != null) {
                    bitmap = resizedBitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Calculate the inSampleSize value
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}