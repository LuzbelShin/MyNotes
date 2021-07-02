package valenzuela.carlos.mynotes

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.File

import java.lang.Exception

class NoteAdapter(context: Context, private var notes: ArrayList<Note>) : BaseAdapter() {
    private var context: Context? = context

    override fun getCount(): Int {
        return notes.size
    }

    override fun getItem(position: Int): Any {
        return notes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.nota_layout, null)
        val note = notes[position]

        val title: TextView = view.findViewById(R.id.tv_title_det)
        title.text = note.title
        val content: TextView = view.findViewById(R.id.tv_content_det)
        content.text = note.title

        val delete: ImageView = view.findViewById(R.id.buttonDelete)
        delete.setOnClickListener{
            delete(note.title)
            notes.remove(note)
            this.notifyDataSetChanged()
        }

        return view
    }

    private fun delete(title: String){
        if(title != ""){
            Toast.makeText(context, "Note doesn't exist", Toast.LENGTH_SHORT).show()
        }else{
            try{
                val archive = File(location(), "$title.txt")
                archive.delete()

                Toast.makeText( context, "Note deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                Toast.makeText( context, "Note couldn't be deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun location():String{
        val album = File(Environment.DIRECTORY_DOCUMENTS,"${File.separator}notes")
        if(album.exists()){
            album.mkdir()
        }

        return album.absolutePath
    }
}