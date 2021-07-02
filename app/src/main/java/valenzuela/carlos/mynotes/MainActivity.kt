package valenzuela.carlos.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

class MainActivity : AppCompatActivity() {
    private var notes = ArrayList<Note>()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readNotes()

        val listView: ListView = findViewById(R.id.listview)
        adapter = NoteAdapter(this, notes)
        listView.adapter = adapter

        val button: FloatingActionButton = findViewById(R.id.button)

        button.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun readNotes(){
        notes.clear()
        val folder = File(location())

        if(folder.exists()){
            val files = folder.listFiles()
            if(files != null){
                for (file in files){
                    readFile(file)
                }
            }
        }
    }

    private fun readFile(file: File){
        val fis = FileInputStream(file)
        val di = DataInputStream(fis)
        val br = BufferedReader(InputStreamReader(di))
        var strLine: String? = br.readLine()
        var myData = ""

        while(strLine != null){
            myData += strLine
            strLine = br.readLine()
        }
        br.close()
        di.close()
        fis.close()

        val name = file.name.substring(0, file.name.length-4)
        val note = Note(name, myData)
        notes.add(note)
    }

    private fun location(): String {
        val folder = File(Environment.DIRECTORY_DOCUMENTS, "${File.separator}notes")
        if (!folder.exists()) {
            folder.mkdir()
        }

        return folder.absolutePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123){
            readNotes()
            adapter.notifyDataSetChanged()
        }
    }
}