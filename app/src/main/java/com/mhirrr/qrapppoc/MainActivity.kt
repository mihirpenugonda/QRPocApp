package com.mhirrr.qrapppoc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images.Media.insertImage
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.mhirrr.qrapppoc.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    var name = "";
    var upi = "";

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra("name").toString()
        upi = intent.getStringExtra("upi").toString()

        with(binding) {
            editPayeeNameText.text = name
            editPayeeNameWalletId.text = "Wallet UPI ID: ${upi}"

            payeeNameText.text = name
            payeeNameWalletId.text = "UPI ID: ${upi}"
        }

        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(name + upi, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            binding.payeeQrCode.setImageBitmap(bmp)
            binding.editPayeeQrCode.setImageBitmap(bmp)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        binding.editShareQrButton.setOnClickListener {
            val currentAmount = binding.editAmount.text

            val shareBitMap = Bitmap.createBitmap(
                binding.shareLayout.width,
                binding.shareLayout.height,
                Bitmap.Config.ARGB_8888
            )
            val shareCanvas = Canvas(shareBitMap)
            binding.shareLayout.draw(shareCanvas)

            val path: String = insertImage(
                contentResolver,
                shareBitMap,
                "Image Description",
                null
            )
            val uri = Uri.parse(path)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share Image"))
        }
    }
}