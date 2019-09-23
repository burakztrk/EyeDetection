package com.mobelm.eyedetection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import kotlinx.android.synthetic.main.activity_main.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.Log
import androidx.core.util.forEach
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {

            val options = BitmapFactory.Options()
            options.inMutable = true
            val myBitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.img1, options)

            val myRectPaint = Paint()
            myRectPaint.strokeWidth = 5f
            myRectPaint.color = Color.RED
            myRectPaint.style = Paint.Style.STROKE

            val tempBitmap = Bitmap.createBitmap(myBitmap.width, myBitmap.height, Bitmap.Config.RGB_565)
            val tempCanvas = Canvas(tempBitmap)
            tempCanvas.drawBitmap(myBitmap, 0f, 0f, null)

            val faceDetector = FaceDetector.Builder(applicationContext).setTrackingEnabled(false).build()
            if (!faceDetector.isOperational)
            {
                AlertDialog.Builder(it.getContext()).setMessage("Could not set up the face detector!").show()
                return@setOnClickListener
            }

            val frame = Frame.Builder().setBitmap(myBitmap).build()
            val faces = faceDetector.detect(frame)


            for (i in 0 until faces.size()) {
                val thisFace = faces.valueAt(i)
                val x1 = thisFace.position.x
                val y1 = thisFace.position.y
                val x2 = x1 + thisFace.width
                val y2 = y1 + thisFace.height
                val left = thisFace.isLeftEyeOpenProbability
                val right = thisFace.isRightEyeOpenProbability
                println("left:$left     right:$right")
                tempCanvas.drawRoundRect(RectF(x1, y1, x2, y2), 2f, 2f, myRectPaint)
            }
            imgview.setImageDrawable(BitmapDrawable(resources, tempBitmap))
        }
    }
}
