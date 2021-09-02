package com.bms.notecontentprovider.dataBase

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.content.UriMatcher.NO_MATCH
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.ContactsContract.Intents.Insert.NOTES
import com.bms.notecontentprovider.dataBase.NotesDatabaseHelper.Companion.TABLE_NOTES

class NotesProvider : ContentProvider() {

    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper: NotesDatabaseHelper

    //inicializador de tudo no provider: instancia db, url, etc
    override fun onCreate(): Boolean {
       mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
       mUriMatcher.addURI(AUTHORITY,"notes", NOTES)
       mUriMatcher.addURI(AUTHORITY,"notes/#", NOTES_BY_ID)

        if (context != null){
            dbHelper = NotesDatabaseHelper(context as Context)
        }
        return true
    }

    //deletar dados
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if (mUriMatcher.match(uri) == NOTES_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val linesAffect = db.delete(TABLE_NOTES,"$_ID=?", arrayOf(uri.lastPathSegment) )
            db.close()
            //tudo que é feito no provider necessario dar notifyChange
            context?.contentResolver?.notifyChange(uri, null)
            return linesAffect


        }else{
            throw UnsupportedSchemeException("Uri invalida para exclusão")
        }
    }


    //valida uri - neste exemplo não sera necessario
    override fun getType(uri: Uri): String? =  throw UnsupportedSchemeException("Uri não implementado")

    //inserir dados, id criado auto, criar elementos notes
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (mUriMatcher.match(uri) == NOTES){
            val db:SQLiteDatabase = dbHelper.writableDatabase
            val id = db.insert(TABLE_NOTES,null,values)
            val insertUri = Uri.withAppendedPath(BASE_URI, id.toString())
            db.close()
            //tudo que é feito no provider necessario dar notifyChange
            context?.contentResolver?.notifyChange(uri, null)
            return insertUri

        }else{
            throw UnsupportedSchemeException("Uri invalida para exclusão")
        }
    }

    //select
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
       return when{
           mUriMatcher.match(uri) == NOTES -> {
               val db:SQLiteDatabase = dbHelper.writableDatabase
               val cursor =
                       db.query(TABLE_NOTES, projection, selection, selectionArgs, null, null, sortOrder)
               cursor.setNotificationUri(context?.contentResolver, uri)
               cursor
           }
           mUriMatcher.match(uri) == NOTES_BY_ID-> {
               val db:SQLiteDatabase = dbHelper.writableDatabase
               val cursor =
                       db.query(TABLE_NOTES, projection, "$_ID = ?", arrayOf(uri.lastPathSegment), null, null, sortOrder)
               cursor.setNotificationUri(context?.contentResolver, uri)
               cursor
           }else -> {
               throw UnsupportedSchemeException("Uri não implementada")
           }
    }}

    //atualiza o id como base pra atualizar
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if (mUriMatcher.match(uri) == NOTES_BY_ID){
            val db:SQLiteDatabase = dbHelper.writableDatabase
            val linesAffect = db.update(TABLE_NOTES,values,"$_ID = ?", arrayOf(uri.lastPathSegment))
            db.close()
            //tudo que é feito no provider necessario dar notifyChange
            context?.contentResolver?.notifyChange(uri, null)
            return linesAffect
        }else{
            throw UnsupportedSchemeException("Uri invalida para exclusão")
        }

    }

    //sempre criar
    companion object{
        //defini o endereço do provider
        const val AUTHORITY = "com.bms.notecontentprovider.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        val URI_NOTES = Uri.withAppendedPath(BASE_URI, "notes")

        //endereços que serão tradados
        const val NOTES = 1
        const val NOTES_BY_ID = 2
    }
}