package valenzuela.carlos.mynotes

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class AddNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val saveButton: Button = findViewById(R.id.btn_save)


        saveButton.setOnClickListener {
            saveNote()
        }

    }

    private fun location(): String{
        val folder = File(Environment.getExternalStorageDirectory(), "notes")
        if(!folder.exists()){
            folder.mkdir()
        }

        return folder.absolutePath
    }

    private fun save(){
        val title: EditText = findViewById(R.id.et_title)
        val titleString: String = title.text.toString()
        val content: EditText = findViewById(R.id.et_content)
        val contentString: String = content.text.toString()

        if(titleString != "" && contentString != ""){
            Toast.makeText(this, titleString, Toast.LENGTH_SHORT).show()
            //Toast.makeText(this, "Error: Void", Toast.LENGTH_SHORT).show()
        }else{
            try{
                val archive = File(location(), "$titleString.txt")
                val fos = FileOutputStream(archive)
                fos.write(contentString.toByteArray())
                fos.close()
                Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show()
            } catch (e: Exception){
                Toast.makeText(this, "Error: Note couldn't be saved", Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }

    private fun saveNote(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 235)
        } else{
            save()
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<String>, grantResults: IntArray ) {
        when(requestCode) {
            235 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    save()
                } else {
                    Toast.makeText(this, "Error: Access Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}