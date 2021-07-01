package valenzuela.carlos.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

class MainActivity : AppCompatActivity() {
    var notes = ArrayList<Note>()
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readNotes()

        val listView: ListView = findViewById(R.id.listview) as ListView
        adapter = NoteAdapter(this, notes)
        listView.adapter = adapter

        val button: FloatingActionButton = findViewById(R.id.button)

        button.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun location(): String{
        val folder = File(Environment.getExternalStorageDirectory(), "notes")
        if(!folder.exists()){
            folder.mkdir()
        }

        return folder.absolutePath
    }

    private fun readFile(archive: File){
        val fis = FileInputStream(archive)
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

        val name = archive.name.substring(0, archive.name.length-4)
        val note = Note(name, myData)
        notes.add(note)
    }

    private fun readNotes(){
        notes.clear()
        val folder = File(location())

        if(folder.exists()){
            val archives = folder.listFiles()
            if(archives != null){
                for (archive in archives){
                    readFile(archive)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123){
            readNotes()
            adapter.notifyDataSetChanged()
        }
    }
}