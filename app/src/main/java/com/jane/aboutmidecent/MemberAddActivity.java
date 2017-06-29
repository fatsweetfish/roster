package com.jane.aboutmidecent;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.security.AccessController.getContext;

public class MemberAddActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private EditText etDesc;
    private ImageView ivImage;
    private Button btnAdd;
    private Button btnDelete;
    private Button btnModify;
    private DBMangement mgr;
    private Intent intent;
    private String selectName;
    private Member selectMember;

    private Uri imageUri;
    private Uri imageCropUri;
    private String imagePath = null;
    private String selection;
    private Uri imgPathUri;
    //private Intent choseIntentData;
    private int currentapiVersion;
    private File imageFile;
    private String mImageDir = null;

    private static final int TAKE_PHOTO=1;
    private static final int CROP_PHOTO=2;
    private static final int CHOOSE_PHOTO=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mgr = new DBMangement(this);
        intent=getIntent();
        String request = intent.getStringExtra("request");

        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        mImageDir = Environment.getExternalStorageDirectory()+"/meta/";

        System.out.println("onCreate...0");
        etName= (EditText) findViewById(R.id.add_name_textView);
        etEmail= (EditText) findViewById(R.id.add_email_textView);
        etDesc= (EditText) findViewById(R.id.add_desc_textView);
        btnAdd= (Button) findViewById(R.id.btn_add_member);
        btnDelete= (Button) findViewById(R.id.btn_delete_member);
        btnModify= (Button) findViewById(R.id.btn_modify_member);
        ivImage= (ImageView) findViewById(R.id.member_add_imageView);




        switch (request){
            //点击添加按钮进入的，则只显示btnAdd
            case "Add":
                btnModify.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
                ivImage.setImageResource(R.drawable.dfimg);
                btnAdd.setOnClickListener(onclick);
                ivImage.setOnClickListener(onclick);
                //btnAdd.setOnClickListener(this);
                break;
            //通过ListView Item进入的
            case "Edit":
                selectName = intent.getStringExtra("name");
                selectMember = mgr.getMember(selectName);
                btnModify.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);

                //Bitmap bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream(selectMember.getImage()));
                Bitmap bitmap;
                String path = selectMember.getImage();

                if(path.contains("res/drawable")){
                    bitmap= BitmapFactory.decodeStream(getClass().getResourceAsStream(path));
                }
                else{
                    bitmap = BitmapFactory.decodeFile(path);
                }
                ivImage.setImageBitmap(bitmap);


                //ivImage.setImageResource(selectMember.getImageId());
                etName.setText(selectName.toCharArray(),0,selectName.length());
                etEmail.setText(selectMember.getEmail().toCharArray(),0,selectMember.getEmail().length());
                etDesc.setText(selectMember.getDesc().toCharArray(),0,selectMember.getDesc().length());

                btnModify.setOnClickListener(onclick);
                btnDelete.setOnClickListener(onclick);
                ivImage.setOnClickListener(onclick);


                break;
        }

        getPermission();







        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void showDialog(Context context)
    {
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setItems(
                new String[] { "拍摄照片", "从相册选择" },
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                //takePicture();
                                Toast.makeText(MemberAddActivity.this, "Take Photo", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                getPhotoFromAlbum();
                                Toast.makeText(MemberAddActivity.this, "Chose Photo From Album", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    /*View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //Intent intent = new Intent(this, MainActivity.class);
            //intent.putExtra("data", "mainActivity");
            //startActivity(intent);
            Member newMember=new Member(etName.getText().toString(),R.drawable.dfimg,etEmail.getText().toString(),etDesc.getText().toString());
            mgr.addMember(newMember);
            setResult(RESULT_OK, intent);
            finish();
        }
    };*/

    /*@Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_member:
                Member newMember=new Member(etName.getText().toString(),R.drawable.dfimg,etEmail.getText().toString(),etDesc.getText().toString());
                mgr.addMember(newMember);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_delete_member:
                mgr.deleteMember(selectName);
                setResult(2,intent);
                finish();
                break;
            case R.id.btn_modify_member:
                Member newMember=new Member(etName.getText().toString(),ivImage.getId(),etEmail.getText().toString(),etDesc.getText().toString());
                mgr.updateMember(selectName, newMember);
                setResult(3, intent);
                finish();
                break;
        }
    }*/


    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_add_member:
                    if(imagePath == null)
                    {
                        imagePath = "/res/drawable/dfimg.png";
                    }

                    System.out.println("add image is "+ imagePath);

                    Member newMember=new Member(etName.getText().toString(),imagePath,etEmail.getText().toString(),etDesc.getText().toString());
                    mgr.addMember(newMember);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case R.id.btn_delete_member:
                    mgr.deleteMember(selectName);
                    setResult(2,intent);
                    finish();
                    break;
                case R.id.btn_modify_member:
                    if(imagePath == null)
                    {
                        imagePath = selectMember.getImage();
                    }
                    Member member=new Member(etName.getText().toString(),imagePath,etEmail.getText().toString(),etDesc.getText().toString());
                    mgr.updateMember(selectName, member);
                    setResult(3, intent);
                    finish();
                    break;
                case R.id.member_add_imageView:
                    showDialog(MemberAddActivity.this);
                    break;
            }
        }
    };


    private void takePicture(){
        System.out.println("takePicture...0");
        Intent itent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File path = new File(mImageDir);
            if (!path.exists()) {
                path.mkdir();
            }
            System.out.println("takePicture...1");

            File imageFile = new File(mImageDir, System.currentTimeMillis() + ".jpg");
            System.out.println("takePicture...2");

            if (currentapiVersion < 24) {
                // 将存储地址转化成uri对象
                imageUri = Uri.fromFile(imageFile);
               // intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //startActivityForResult(intent, TAKE_PHOTO);
            } else {
                System.out.println("takePicture...3 "+ BuildConfig.APPLICATION_ID + getPackageName());
                imageUri = FileProvider.getUriForFile(this,
                        getPackageName() + ".provider", imageFile);
                //imageUri = FileProvider.getUriForFile(MemberAddActivity.this, "com.jane.aboutmidecent.fileprovider", imageFile);
                System.out.println("imagePath1 = " + imageUri.toString());
                System.out.println("imagePath2 = " + FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                        ".provider", imageFile).toString());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);



            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }
    }


    public void takePhoto() {
        // new一个File用来存放拍摄到的照片
        // 通过getExternalStorageDirectory方法获得手机系统的外部存储地址
        System.out.println("takePhoto...0");
        imageFile = new File(Environment
                .getExternalStorageDirectory(), "tempImage.jpg");
        // 如果存在就删了重新创建
        try {
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }






        System.out.println("takePhoto...1");

        // 设置打开照相的Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("takePhoto...2");

        intent.putExtra("return-data", false);

        System.out.println("currentapiVersion====>"+currentapiVersion);
        if (currentapiVersion<24){
            // 将存储地址转化成uri对象
            imageUri = Uri.fromFile(imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //startActivityForResult(intent, TAKE_PHOTO);
        }else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());
            //Uri uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            imageUri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //startActivityForResult(intent, TAKE_PHOTO);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, TAKE_PHOTO);



        // 设置相片的输出uri为刚才转化的imageUri
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        //System.out.println("takePhoto...3");
        // 开启相机程序，设置requestCode为TOKE_PHOTO
        //startActivityForResult(intent, TAKE_PHOTO);
        System.out.println("takePhoto...");

    }


    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CROP_PHOTO);
    }


    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            System.out.println("getImageContentUri find content");
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                /*ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
                System.out.println("getImageContentUri fine imagUri");
                return imageUri;
            } else {
                System.out.println("getImageContentUri file not exist");
                return null;
            }
        }
    }


    public void getPhotoFromAlbum() {
        //同样new一个file用于存放照片
        /*File imageFile = new File(Environment
                .getExternalStorageDirectory(), "outputImage.jpg");
        if (imageFile.exists()) {
            imageFile.delete();
        }
        try {
             imageFile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }*/
        //转换成Uri

        //imageUri = Uri.fromFile(imageFile);
        //开启选择呢绒界面
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //设置可以缩放
        intent.putExtra("scale", true);
        //设置可以裁剪
        intent.putExtra("crop", true);
        intent.setType("image/*");
        //设置输出位置
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //开始选择
        startActivityForResult(intent, CHOOSE_PHOTO);
        System.out.println("choose... out="+MediaStore.EXTRA_OUTPUT);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 从拍照界面返回
            case TAKE_PHOTO:
                System.out.println("take photo receive..." + resultCode +"(" +RESULT_OK+")");
                if (resultCode == RESULT_OK) {
                    /*System.out.println("take photo receive in");
                    File imageCropFile = new File(Environment
                            .getExternalStorageDirectory(), "tempCropImage.jpg");
                    // 如果存在就删了重新创建
                    try {
                        if (imageCropFile.exists()) {
                            imageCropFile.delete();
                        }
                        imageCropFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (currentapiVersion<24){
                        // 将存储地址转化成uri对象
                        imageCropUri = Uri.fromFile(imageCropFile);
                    }else {
                        ContentValues contentValues = new ContentValues(1);
                        contentValues.put(MediaStore.Images.Media.DATA, imageCropFile.getAbsolutePath());
                        imageCropUri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    }

                    // 设置intent为启动裁剪程序
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageCropUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra("crop", true);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    // outputX outputY 是裁剪图片宽高
                    intent.putExtra("outputX", 340);
                    intent.putExtra("outputY", 340);
                    intent.putExtra("return-data", false);

                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                    //imageCropUri = Uri.fromFile(imageCropFile);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
                    startActivityForResult(intent, CROP_PHOTO);*/
                    cropRawPhoto(getImageContentUri(imageFile));

                }

                break;
            // 从裁剪界面返回
            case CROP_PHOTO:
                System.out.println("crop photo receive... "+ resultCode );
                //System.out.println(" imageuri=" + imageUri.toString());
                //Bundle extras = data.getExtras();
                if(data == null)
                {
                    System.out.println("data is null");
                }
                if (resultCode == RESULT_OK) {
                    System.out.println("crop photo receive in");
                    Bitmap bitmap;
                    try {
                        //通过BitmapFactory将imageUri转化成bitmap
                        bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        //设置显示
                        ivImage.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case CHOOSE_PHOTO:
                System.out.println("chose photo receive...");
                if(resultCode==RESULT_OK){
                    //System.out.println("chose photo result ok...");
                    //choseIntentData = data;
                    //getPermission();
                    handleImageOnKitkat(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void handleImageOnKitkat(Intent data) {
        //String imagePath = null;
        Uri uri = data.getData();
        //System.out.println("handleImageOnKitkat 1...");
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            //System.out.println("handleImageOnKitkat 2...");
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri
                    .getAuthority())) {
                //System.out.println("handleImageOnKitkat 3...");
                String id = docId.split(":")[1]; // 解析出数字格式的id
                selection = MediaStore.Images.Media._ID + "=" + id;
                System.out.println("handleImageOnKitkat selection = "+selection + " rul=" + MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imgPathUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
               // getPermission();
                //imagePath = getImagePath(
                 //       MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                System.out.println("Image1...");
            } else if ("com.android.providers.downloads.documents".equals(uri
                    .getAuthority())) {
                System.out.println("handleImageOnKitkat 4...");
                /*Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));*/
                //imagePath = getImagePath(contentUri, null);
                imgPathUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                selection = null;
                //getPermission();
                System.out.println("Image2...");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果不是document类型的Uri，则使用普通方式处理
            System.out.println("handleImageOnKitkat 5...");
            //imagePath = getImagePath(uri, null);
            imgPathUri = uri;
            selection = null;
            //getPermission();
            System.out.println("Image3...");
        }
        imagePath = getImagePath(
                imgPathUri, selection);

        if(imagePath != null) {
            displayImage(imagePath); // 根据图片路径显示图片
            System.out.println(imagePath);
        }
        /*if(imagePath != null) {
            displayImage(imagePath); // 根据图片路径显示图片
            System.err.println(imagePath);
        }*/
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
            System.out.println("path = " +path);
        }
        else{
            System.err.println("can not find Image");

        }

        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivImage.setImageBitmap(bitmap);
            System.out.println("displayImage...");
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT)
                    .show();
        }
    }



    /*void getPermission()
    {
        System.out.println("getPermission****");
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            System.out.println("getPermission request****");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    124);
        }
        else
        {
            System.out.println("alerady has getPermission ****");
            imagePath = getImagePath(
                    imgPathUri, selection);

            if(imagePath != null) {
                displayImage(imagePath); // 根据图片路径显示图片
                System.out.println(imagePath);
            }
        }
    }*/


    void getPermission()
    {
        System.out.println("getPermission****");
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            System.out.println("getPermission request****");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    124);
        }
        else
        {
            System.out.println("alerady has getPermission ****");

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        System.out.println("onRequestPermissionsResult****");
        if (requestCode == 124) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                System.out.println("heihei "+"获取到权限了！");

                //handleImageOnKitkat(choseIntentData);
                //imagePath = getImagePath(
               //         imgPathUri, selection);

               // if(imagePath != null) {
                //    displayImage(imagePath); // 根据图片路径显示图片
                //    System.out.println(imagePath);
               // }

            }
            else {
                System.out.println("heihei "+"搞不定啊！");
            }
        }
    }


    public static void copyfile(String srcFile, String destFile,Boolean rewrite ){

        File fromFile =  new File(srcFile);
        File toFile =  new File(destFile);
        if(!fromFile.exists()){
            return;
        }

        if(!fromFile.isFile()){
            return;
        }
        if(!fromFile.canRead()){
            return;
        }

        if(toFile.exists()){
           if(rewrite) {
               toFile.delete();
           }
           else
           {
              return;
           }
        }
        else
        {

            if(!toFile.getParentFile().exists()){
                toFile.getParentFile().mkdirs();
            }
        }


        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while((c=fosfrom.read(bt)) > 0){
                fosto.write(bt,0,c);
            }
            //关闭输入、输出流
            fosfrom.close();
            fosto.close();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }








}
