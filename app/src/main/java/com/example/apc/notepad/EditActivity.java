package com.example.apc.notepad;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * メモ編集画面アクティビティ
 */
public class EditActivity extends Activity {
    private EditText editText_title = null;
    private EditText editText_tag = null;
    private EditText editText_main = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_bar_note_edit);

        editText_title = (EditText) findViewById(R.id.editText_title);
        editText_tag = (EditText) findViewById(R.id.editText_tag);
        editText_main = (EditText) findViewById(R.id.editText_main);

        // インテントを取得
        Intent intent = getIntent();

        // インテントに保存されたデータを取得
        String CLICK_NOTE_ID = intent.getStringExtra("CLICK_NOTE_ID");
//        boolean isAnd = intent.getBooleanExtra("AND", true);

        Toast.makeText(this, CLICK_NOTE_ID, Toast.LENGTH_SHORT).show();

        // DataBase を作成
        final DatabaseHelper myDatabaseHelper = new DatabaseHelper(this);
        final SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();

        /**
        // DB から query で値を読み込む
        String[] cols = {"title", "tag", "main"};
        String selection = "_id = ?";
        try {
            db.query("notes", new String[]{"rowid as _id", "", "title"}, null, null, null, null, null);
//            Cursor c = db.query("notes", cols, selection, new String[]{CLICK_NOTE_ID}, null, null, null);
            // 読み込んだ値を TextView に表示
            StringBuilder text = new StringBuilder();
            while (c.moveToNext()) {
                // 各値ごとにデータを取得する
                text.append(c.getString(0));
                text.append(c.getString(1));
                text.append(c.getString(2));
            }
        } finally {
            db.close();
        }

        */
    }

    /**
     * 戻るボタンが押された時の処理
     */
    public void onBack(View view) {
        // アクティビティを終了させて一つ前のアクティビティへ戻る
        finish();
    }

    /**
     * 保存ボタンが押された時の処理
     */
    public void onSave(View view) {

        // 現在日時を yyyy/mm/dd 形式で取得する。
        Date date = new Date();
        // 表示形式を設定
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Toast.makeText(this, (sdf.format(date)), Toast.LENGTH_SHORT).show();

        // インテントを取得
        Intent intent = getIntent();

        // インテントに保存されたデータを取得
        String CLICK_NOTE_ID = intent.getStringExtra("CLICK_NOTE_ID");

        // DataBase を読み込み
        DatabaseHelper myDatabaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();

        // DB レコード書き込み用のコンテントバリューを設定
        ContentValues cv = new ContentValues();
        cv.put("modify_date", (sdf.format(date)));
        cv.put("title", editText_title.getText().toString());
        cv.put("tag", editText_tag.getText().toString());
        cv.put("main", editText_main.getText().toString());

        // DB に書き込み
        try {
            db.insert("notes", null, cv);
        } finally {
            db.close();

            // 保存メッセージを表示
            Toast.makeText(this, R.string.massage_save, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 削除ボタンが押された時の処理
     */
    public void onDelete(View view) {

        // インテントを取得
        Intent intent = getIntent();

        // インテントに保存されたデータを取得
        String CLICK_NOTE_ID = intent.getStringExtra("CLICK_NOTE_ID");

        // DataBase を読み込み
        DatabaseHelper myDatabaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();

        // DB レコード削除用のコンテントバリューを設定
//        ContentValues cv = new ContentValues();
//        cv.put("_id", intent.getStringExtra("CLICK_NOTE_ID");

        // DB のインテント ID レコードを削除
        try {
            db.delete("notes", "_id = ?", new String[]{CLICK_NOTE_ID});
        } finally {
            db.close();

            // メッセージを表示
            Toast.makeText(this, R.string.massage_delete, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}