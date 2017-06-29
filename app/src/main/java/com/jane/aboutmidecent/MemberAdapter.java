package com.jane.aboutmidecent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jane on 2017/5/4.
 */

public class MemberAdapter extends ArrayAdapter <Member>{
    public MemberAdapter(Context context, int resource, List<Member> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 获取成员的数据
        final Member member = getItem(position);

        // 创建布局
        View oneMemberView = LayoutInflater.from(getContext()).inflate(R.layout.member_item, parent, false);

        // 获取ImageView和TextView
        ImageView imageView = (ImageView) oneMemberView.findViewById(R.id.member_small_imageView);
        TextView textView = (TextView) oneMemberView.findViewById(R.id.member_name_textView);

        // 根据成员数据设置ImageView和TextView的展现
        Bitmap bitmap;
        String path = member.getImage();

        if(path.contains("res/drawable")){
            bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream(path));
        }
        else{
            bitmap = BitmapFactory.decodeFile(path);
        }


        imageView.setImageBitmap(bitmap);
        //imageView.setImageResource(member.getImageId());
        textView.setText(member.getName());

        oneMemberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  初始化一个准备跳转到MemberDetailActivity的Intent
                Intent intent = new Intent(getContext(), MemberDetailActivity.class);

                // 往Intent中传入Member相关的数据，供MemberDetailActivity使用
                intent.putExtra("member_image", member.getImage());
                intent.putExtra("member_desc", member.getDesc());
                intent.putExtra("member_email", member.getEmail());

                //  初始化一个准备跳转到MemberDetailActivity的Intent
                getContext().startActivity(intent);
            }
        });

        oneMemberView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View arg0) {
                try{

                    System.out.println("long click " +member.getName() + ",imag path = "+member.getImage());
                    Intent intent = new Intent(getContext(), MemberAddActivity.class);

                    // 往Intent中传入Member相关的数据，供MemberDetailActivity使用
                    intent.putExtra("request", "Edit");
                    intent.putExtra("name", member.getName());

                    //  初始化一个准备跳转到MemberDetailActivity的Intent
                    //getContext().st

                    ((MainActivity)getContext()).startActivityForResult(intent,1);

                }catch(Exception e){
                    //System.out.println(e);
                    //MainActivity.this.info.setText("手机桌面背景设置失败");
                    System.out.println("long click  " + member.getName() +" fail");
                }
                return true;
            }
        });

        return oneMemberView;
    }


}
