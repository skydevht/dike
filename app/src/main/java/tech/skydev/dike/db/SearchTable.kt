package tech.skydev.dike.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import tech.skydev.dike.Injector
import tech.skydev.dike.model.Document
import tech.skydev.dike.model.Section
import tech.skydev.dike.service.DocumentService
import tech.skydev.dike.util.FileUtil
import timber.log.Timber
import java.text.Normalizer


/**
 * Created by hash on 7/6/17.
 */
class SearchTable(val context: Context) {
    val searchHelper: SearchDBHelper

    val COL_ART_ID = "art_id"
    var COL_ART_TEXT = "art_text"
    var COL_ART_NORM_TEXT = "art_norm_text"

    val DATABASE_NAME = "Search"
    val FTS_VIRTUAL_TABLE = "FTS"
    val DATABASE_VERSION = 1


    init {
        searchHelper = SearchDBHelper(context)
    }

    fun getWordMatches(query: String, columns: Array<String>?): Cursor? {
        val selection = COL_ART_NORM_TEXT + " MATCH '$query*'"

        return query(selection, null, columns)
    }

    private fun query(selection: String, selectionArgs: Array<String>?, columns: Array<String>?): Cursor? {
        val builder = SQLiteQueryBuilder()
        builder.tables = FTS_VIRTUAL_TABLE

        val cursor = builder.query(searchHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null)

        if (cursor == null) {
            return null
        } else if (!cursor.moveToFirst()) {
            cursor.close()
            return null
        }
        return cursor
    }

    inner class SearchDBHelper(val context: Context) :
            SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        var FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE $FTS_VIRTUAL_TABLE USING fts3 ('_id' INTEGER PRIMARY KEY AUTOINCREMENT, $COL_ART_ID, $COL_ART_TEXT, $COL_ART_NORM_TEXT)"


        private var mDatabase: SQLiteDatabase? = null

        override fun onCreate(db: SQLiteDatabase) {
            mDatabase = db;
            mDatabase?.execSQL(FTS_TABLE_CREATE);
            buildIndex()
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Timber.w("Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);

        }

        private fun buildIndex() {
            Thread(Runnable {
                    loadDocuments()
            }).start()
        }


        private fun loadDocuments() {
            Injector.documentService?.loadAllDocuments(object : DocumentService.Callback<List<Document>> {
                override fun onSuccess(result: List<Document>) {
                    result.forEach {
                        Injector.documentService?.loadDocument(it.id!!, object : DocumentService.Callback<Document> {
                            override fun onSuccess(doc: Document) {
                                processSection(doc.sections, doc.id!!, doc.path!!)
                            }

                            override fun onError(ex: Exception) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                        })
                    }
                }

                override fun onError(ex: Exception) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }

        private fun processSection(sections: List<Section>?, docId: String, path: String) {
            sections?.let {
                sections.forEach {
                    it.contents?.forEach {
                        val initialValue = ContentValues()
                        initialValue.put(COL_ART_ID,it.name)
                        val text = FileUtil.loadTextFileFromAssets(context, "$path/${it.path}")
                        initialValue.put(COL_ART_TEXT, text)
                        initialValue.put(COL_ART_NORM_TEXT, stripAccents(text))
                        mDatabase?.insert(FTS_VIRTUAL_TABLE, null, initialValue)
                    }
                    processSection(it.children, docId, path)
                }
            }
        }

        private fun stripAccents(s: String): String {
            var s1 = s
            s1 = Normalizer.normalize(s1, Normalizer.Form.NFD)
            s1 = s1.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
            return s1
        }
    }
}
