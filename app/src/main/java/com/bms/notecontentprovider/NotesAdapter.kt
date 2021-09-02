package com.bms.notecontentprovider

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bms.notecontentprovider.dataBase.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.bms.notecontentprovider.dataBase.NotesDatabaseHelper.Companion.TITLE_NOTES

//private val  listener: NoteClickedListener
//enventos de click para o adapter
class NotesAdapter (private val  listener: NoteClickedListener): RecyclerView.Adapter<NotesViewHolder>(){

    private var mCursor: Cursor?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
            NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))

    override fun getItemCount(): Int = if(mCursor != null) mCursor?.count as Int else 0

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        mCursor?.moveToPosition(position)//posição pesquisada

        //passar VALORES para DENTRO DO LAYOUT
        //PEGANDO VALOR DENTRO CURSOR - MCURSOR + GETSTRING + COLUNA ONDE ESTA O VALOR (EX: TITLE_NOTES)
        holder.noteTitle.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTES)as Int)

        holder.noteDescription.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTES)as Int)

        holder.noteButtonRemove.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.noteRemoveItem(mCursor)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            listener.noteClickedItem(mCursor as Cursor) }
    }


    //quando tiver um dado sera notificada
    fun setCursor(newCursor: Cursor?){
        mCursor = newCursor
        notifyDataSetChanged()
    }

}

//viewholder

class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val noteTitle = itemView.findViewById<TextView>(R.id.note_title)
    val noteDescription = itemView.findViewById<TextView>(R.id.note_description)
    val noteButtonRemove = itemView.findViewById<Button>(R.id.note_button_remove)

}