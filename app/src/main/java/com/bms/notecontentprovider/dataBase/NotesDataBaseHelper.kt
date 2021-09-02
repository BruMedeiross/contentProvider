package com.bms.notecontentprovider.dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotesDatabaseHelper(
    context: Context
):SQLiteOpenHelper(context,"dabaseNotes", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
    //criando a tabela
        db?.execSQL(
            "CREATE TABLE $TABLE_NOTES (" +
                "$_ID INTEGER NOT NULL PRIMARY KEY, " +
                "$TITLE_NOTES TEXT NOT NULL, " +
                "$DESCRIPTION_NOTES TEXT NOT NULL )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion:Int, newVersion:Int) {
    }

    companion object{
        //nome tabela, 1 letra maiuscula e colunas tudo minusculo - diferenciar
        const val TABLE_NOTES: String = "Notes"
        const val TITLE_NOTES: String = "title"
        const val DESCRIPTION_NOTES: String = "description"
}

}