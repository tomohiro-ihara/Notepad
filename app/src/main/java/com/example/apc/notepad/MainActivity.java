package com.example.apc.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

//add comment
/**
 * 一覧表示アクティビティ
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // 表示するカラム名
    public static final String[] FROM = {"title", "modify_date"};

    // Adapter でバインドする View の ID を指定
    private static final int[] TO = {R.id.textView_list1, R.id.textView_list2};

    // ListView に DB の値をバインドするアダプタを指定
    private SimpleCursorAdapter adapter;

    /**
     * アクティビティ初期作成時
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 自動生成された R.java の定数を指定して XML からレイアウトを生成
        setContentView(R.layout.activity_main);

        /**
         * drawer レイアウト, ツールバーの処理
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    /**
     * アクティビティが前面に来るたびにデータを更新
     */
    @Override
    protected void onResume() {
        super.onResume();

        // XMLで定義した android:id の値を指定して ListView を取得
        ListView myListView = (ListView) findViewById(R.id.listView_fileList);

        // DataBase を作成
        final DatabaseHelper myDatabaseHelper = new DatabaseHelper(this);
        final SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();

        // DB からデータを抽出
        Cursor c = db.query("notes", new String[]{"rowid as _id", "modify_date", "title"}, null, null, null, null, null);

        // ListViewに表示する要素を保持するアダプタを生成
        adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_item, c, FROM, TO, 0);

        // アダプタを設定
        myListView.setAdapter(adapter);

        // bind して ListViewに表示
        myListView.setEmptyView(findViewById(R.id.emptyView));

        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        /**
         * メモ新規作成ボタンのクリック時の処理
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), EditActivity.class);
                // EditActivity を表示
                startActivity(intent);

                // DB のクローズ処理
                db.close();
            }
        });

        /**
         * リストをショートタップで編集画面
         */
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                // タップした箇所の文字列を取得
                ListView list = (ListView) parent;
                Cursor c = (Cursor) list.getItemAtPosition(position);

                // 0:_id, を取得
                final String strId = c.getString(0);

                // インテントに値をセット
                Intent intent = new Intent(getApplication(), EditActivity.class);
                intent.putExtra("CLICK_NOTE_ID", strId);
                // EditActivity を表示
                startActivity(intent);

                // DB のクローズ処理
                db.close();
            }
        });

        /**
         * リストをロングタップで削除
         */
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                // 長押しした箇所の文字列を取得
                ListView list = (ListView) parent;
                Cursor c = (Cursor) list.getItemAtPosition(position);

                // 0:_id, 1:modify_date, 2:title を取得
                final String strId = c.getString(0);
                final String strModify_date = c.getString(1);
                final String strTitle = c.getString(2);

                // アラートダイアログを設定
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("削除");
                builder.setMessage(strModify_date + " に作成した『" + strTitle + "』を削除してもいいですか？");

                // OK の時の処理
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // DB から該当のメモを削除
                        db.delete("notes", "_id = " + strId, null);
                        // リストビューを再描画
                        onResume();
                    }
                });
                // キャンセルの時の処理
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 何もせずに終了
                    }
                });

                // ロングタップ時のダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }

    /**
     * 戻るボタンの処理
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * オプションメニューの処理
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.apc.notepad/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.apc.notepad/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
