package com.bms.notecontentprovider

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bms.notecontentprovider.dataBase.NotesDatabaseHelper.Companion.TITLE_NOTES
import com.bms.notecontentprovider.dataBase.NotesProvider.Companion.URI_NOTES
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var noteRecyclerView: RecyclerView
    lateinit var noteAdd: FloatingActionButton

    lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteAdd = findViewById(R.id.notes_add)
        noteAdd.setOnClickListener {
            NotesDetailFragment().show(supportFragmentManager, "dialog")
        }

        adapter = NotesAdapter(object : NoteClickedListener {
            override fun noteClickedItem(cursor: Cursor) {
                val id = cursor.getLong(cursor.getColumnIndex(_ID))
                val fragment = NotesDetailFragment.newInstance(id)
                fragment.show(supportFragmentManager, "dialog")
            }

            override fun noteRemoveItem(cursor: Cursor?) {
                val id = cursor?.getLong(cursor.getColumnIndex(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTES, id.toString()), null,null)
            }
        })

        adapter.setHasStableIds(true)//para q não tenha ids repetidos no adapter

        noteRecyclerView = findViewById(R.id.notes_recycler)
        noteRecyclerView.layoutManager= LinearLayoutManager(this)
        noteRecyclerView.adapter= adapter//passar adapter pro recyclerv

        LoaderManager.getInstance(this).initLoader(0, null, this)
    }

    //METODOS LOADER MANAGER;O Loader monitorará as alterações nos dados e as relatará a você por meio de novas chamadas aqui.
    //inicializador
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
            //deve ser construído com todas as informações para a consulta para executar,
        CursorLoader(this, URI_NOTES, null, null, null, TITLE_NOTES)

    //manipular dados recebidos
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) {adapter.setCursor(data)
        }
    }

    //acabar com pesquisa em 2 plano
    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.setCursor(null)
    }
}