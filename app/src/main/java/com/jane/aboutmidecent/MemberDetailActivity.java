package com.jane.aboutmidecent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // 从Intent获取数据
        String image = getIntent().getStringExtra("member_image");
        String desc = getIntent().getStringExtra("member_desc");
        final String email = getIntent().getStringExtra("member_email");

        // 获取特定的视图
        ImageView imageView = (ImageView) findViewById(R.id.member_large_imageView);
        TextView textView = (TextView) findViewById(R.id.member_desc_textView);

        // 根据数据设置视图展现
       // imageView.setImageResource(imageId);
        //Bitmap bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream(image));
        Bitmap bitmap;

        if(image.contains("res/drawable")){
            bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream(image));
        }
        else{
            bitmap = BitmapFactory.decodeFile(image);
        }
        imageView.setImageBitmap(bitmap);
        textView.setText(desc);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, email, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
