package com.jane.aboutmidecent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jane on 2017/5/4.
 */

public class DBMangement {


    private static final String DATABASE_NAME = "members.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "members";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String DESC = "desc";
    private static final String IMAGE = "image";

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBMangement(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add list
     * @param memberList
     */
    public void addList(List<Member> memberList) {
        db.beginTransaction();  //开始事务
        try {
            for (Member member : memberList) {
                db.execSQL("INSERT INTO members VALUES(null, ?, ?, ?, ?)", new Object[]{member.getName(), member.getEmail(), member.getDesc(), member.getImage()});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * add member
     * @param member
     */
    public void addMember(Member member) {
        db.beginTransaction();  //开始事务
        try {
                db.execSQL("INSERT INTO members VALUES(null, ?, ?, ?, ?)", new Object[]{member.getName(), member.getEmail(), member.getDesc(), member.getImage()});

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update member's image
     * @param member
     */
    public void updateImage(Member member) {
        ContentValues cv = new ContentValues();
        cv.put("image", member.getImage());
        db.update("members", cv, "name = ?", new String[]{member.getName()});
    }

    /**
     * update member's email
     * @param member
     */
    public void updateEmail(Member member) {
        ContentValues cv = new ContentValues();
        cv.put("email", member.getEmail());
        db.update("members", cv, "name = ?", new String[]{member.getName()});
    }


    /**
     * update member's desc
     * @param member
     */
    public void updateDesc(Member member) {
        ContentValues cv = new ContentValues();
        cv.put("desc", member.getDesc());
        db.update("members", cv, "name = ?", new String[]{member.getName()});
    }


    public void updateMember(String name, Member member) {
        ContentValues cv = new ContentValues();
        cv.put("name", member.getName());
        cv.put("email", member.getEmail());
        cv.put("desc", member.getDesc());
        cv.put("image", member.getImage());
        db.update("members", cv, "name = ?", new String[]{name});
    }

    /**
     * delete a member
     * @param member
     */
    public void deleteMember(String name) {
        db.delete("members", "name = ?", new String[]{name});
    }

    /**
     * query all member, return list
     * @return List<Member>
     */
    public List<Member> queryList() {
        List<Member> memberList = new ArrayList<Member>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Member member = new Member(c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("image")),c.getString(c.getColumnIndex("email")),c.getString(c.getColumnIndex("desc")));
            member._id = c.getInt(c.getColumnIndex("_id"));
            memberList.add(member);
        }
        c.close();
        return memberList;
    }

   /* public Integer getMemberCount() {

        String col[] = { NAME, EMAIL, DESC, IMAGE };
        //查询数据
        Cursor cur = db.query(TABLE_NAME, col, null, null, null, null, null);
        Integer num = cur.getCount();
        return num;

    }*/

    public long queryCount( ){
        String sql = "select count(*) from members";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    public int getMemberCounts(){
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        Cursor cursor=db.rawQuery(selectQuery,null);
        int count =  cursor.getCount();
        cursor.close();
        return count;
    }

    public Member getMember(String name){

        //Cursor对象返回查询结果
        Cursor cursor=db.query(TABLE_NAME,new String[]{"_id",NAME,EMAIL,DESC,IMAGE},
                "name = ?",new String[]{name},null,null,null,null);

        Member member=null;
        //注意返回结果有可能为空
        if(cursor.moveToFirst()){
            //member =new Member(cursor.getString(1),cursor.getInt(4),cursor.getString(2), cursor.getString(3));
            member =new Member(cursor.getString(1),cursor.getString(4),cursor.getString(2), cursor.getString(3));
        }
        return member;
    }

    /**
     * query all members, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM members", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }


}
