package com.example.apc.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite 処理 Helper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Databaseファイル名
    static final private String DB_NAME = "sqlite.db";

    // Databaseバージョン
    static final private int DB_VER = 1;

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VER);
    }

    // onCreate メソッドはデータベースを初めて使用する時に実行される処理
    // 処理は MainActivity に移植
    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("DROP TABLE IF EXISTS notes");

        // 初期テーブル作成
        db.execSQL("CREATE TABLE notes " +
                "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", modify_date TEXT" +
                ", title TEXT" +
                ", tag TEXT" +
                ", main TEXT" +
                ")");

        // 初期サンプルデータ投入
        db.execSQL("INSERT INTO notes(_id, modify_date, title, tag, main)" +
                " VALUES('1', 'YYYY/MM/DD', 'title', 'タグ', '本文')");
        db.execSQL("INSERT INTO notes(_id, modify_date, title, tag, main)" +
                " VALUES('2', '2015/12/09', 'タイトル２', 'タグ', '本文')");
        db.execSQL("INSERT INTO notes(_id, modify_date, title, tag, main)" +
                " VALUES('3', '2015/12/10', 'タイトル３', 'タグ', '本文')");
        db.execSQL("INSERT INTO notes(_id, modify_date, title, tag, main)" +
                " VALUES('4', '2015/12/11', 'タイトル４', 'タグ', '本文')");
        db.execSQL("INSERT INTO notes(_id, modify_date, title, tag, main)" +
                " VALUES('5', '2015/12/12', 'タイトル５', 'タグ', '本文')");

    }

    // onUpgradeメソッド
    // onUpgrade()メソッドはデータベースをバージョンアップした時に呼ばれる
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }
}
