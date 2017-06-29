package com.jane.aboutmidecent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DBMangement mgr;
    private String TAG ="myapp";
    private List<Member> mList;
    private ListView mListView;
    private MemberAdapter memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "support@mid-cent.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mgr = new DBMangement(this);
        //long num = mgr.queryCount();
        int num = mgr.getMemberCounts();
        if(num == 0) {
            Log.i(TAG,"db has no data, creat base data");
            System.out.println("db has no data, creat base data");
            addBase();
        }
        else{
            Log.i(TAG,"db has  " + num);
            System.out.println("db has  " + num);

            //String testPath = getApplicationContext().getFilesDir().getAbsolutePath();
            //System.out.println("dir is   " +  testPath);

           // String pkgPath = getApplicationContext().getPackageResourcePath();
            //System.out.println("pkg path is    " +  pkgPath);
        }
        //通过ID获取listView
        mListView = (ListView) findViewById(R.id.member_listView);

        showListView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    public void addBase() {
        ArrayList<Member> members = new ArrayList<Member>();
        members.add(new Member("Jane","/res/drawable/jane.png","janezheng@xxx.com","Jane, famle, milo's mama "));
        members.add(new Member("David","/res/drawable/david.png","davidzhu@xxx.com","David, male, milo's papa"));
        members.add(new Member("Milo","/res/drawable/milo.png","milozhu@xxx.com","Milo, baby boy, 3 years old"));
        mgr.addList(members);
    }

    public void showListView() {
        mList = mgr.queryList();
        memberAdapter = new MemberAdapter(this, R.layout.member_item,mList );
        //设置listView的Adapter
        mListView.setAdapter(memberAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        switch(id){
            case R.id.action_settings:
                return true;
            case R.id.add:
                Toast.makeText(this, "Add...", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this,MemberAddActivity.class);
                i.putExtra("request","Add");
                startActivityForResult(i, 1);
                break;
            //case R.id.modify:
            //    Toast.makeText(this, "Modify...", Toast.LENGTH_SHORT).show();
            //    break;
            //case R.id.delete:
            //    Toast.makeText(this, "Delete...", Toast.LENGTH_SHORT).show();
            //    break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //根据返回的resultCode判断是通过哪种操作返回的，并提示相关信息；
        switch (requestCode){
            case 0:
                if (resultCode==2)
                    Toast.makeText(this,"Delete Success",Toast.LENGTH_SHORT).show();
                if (resultCode==3)
                    Toast.makeText(this,"Modify Success",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (resultCode==RESULT_OK)
                    Toast.makeText(this,"Add Success",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"Option Null",Toast.LENGTH_SHORT).show();
                return;

        }
        /**
         * 如果这里仅仅使用adapter.notifyDataSetChanged()是不会刷新界面ListView的，
         * 因为此时adapter中传入的memberList并没有给刷新，即adapter也没有被刷新，所以你可以
         * 重新获取memberList后再改变adapter
         */

        showListView();
    }
}
