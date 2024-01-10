package com.isaaclabs.accessfilespoc

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.isaaclabs.accessfilespoc.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val REQUEST_IMAGE_GET = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.openGallery.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            if(intent.resolveActivity(this.packageManager) != null){
                startActivityForResult(intent,REQUEST_IMAGE_GET)
            } else {
                this?.let{
                    Toast.makeText(this,"Ops, não foi encontrado um app para continuar a seleção",Toast.LENGTH_SHORT)
                    }
                }
            }

        binding.openDocs.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            if(intent.resolveActivity(this.packageManager) != null){
                startActivityForResult(intent,REQUEST_IMAGE_GET)
            } else {
                this?.let{
                    Toast.makeText(this,"Ops, não foi encontrado um app para continuar a seleção",Toast.LENGTH_SHORT)
                }
            }
        }



        setContentView(binding.root)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this?.let { context ->
            data?.data?.let { uri ->
                val cursor: Cursor? = this.contentResolver.query(
                    uri, null, null, null, null, null)
                cursor?.let {
                    if (it.moveToFirst()) {
                        val nameIndex: Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
                        val displayName: String =
                            it.getString(nameIndex)
                        val size: Long = if (!it.isNull(sizeIndex)) {
                            it.getString(sizeIndex).toLong()
                        } else {
                            -1
                        }
                        val extension: String = displayName.split(".").last()
                        cursor.close()


                        val inputStream: InputStream =
                            context.contentResolver.openInputStream(uri)!!
                        val newFile = File(context.filesDir, displayName)
                        val out: OutputStream = FileOutputStream(newFile)
                        val buf = ByteArray(1024)
                        var len: Int
                        while (inputStream.read(buf).also { len = it } > 0) {
                            out.write(buf, 0, len)
                        }
                        out.close()
                        inputStream?.close()


                    }
                }
            }
        }



    }


}