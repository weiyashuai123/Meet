package cn.bmob.imdemo.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.ImageLoaderFactory;
import cn.bmob.imdemo.bean.DongTai;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.util.Util;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class DongTaiActivity extends AppCompatActivity {


    EditText et_conmtant;

    TextView txt_image;

    ImageView img_img;

    ImageView img_back;

    TextView txt_fb;



    private DongTai dongtai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_tai);
        dongtai = new DongTai(BmobUser.getCurrentUser(User.class));
        initView();

    }

    private void initView() {
        img_img = (ImageView) findViewById(R.id.img_image);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txt_fb = (TextView) findViewById(R.id.txt_fb);
        txt_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FaBiao();
            }
        });
        txt_image = (TextView) findViewById(R.id.txt_image);
        txt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void FaBiao() {
        et_conmtant = (EditText) findViewById(R.id.et_contant);
        dongtai.setContant(et_conmtant.getText().toString());
        String a = dongtai.getContant();
        String b = dongtai.getImage();
        if (!a.isEmpty()||!b.isEmpty()){
            dongtai.setAgree(0);
            dongtai.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){
                        Toast.makeText(DongTaiActivity.this, "成功发表", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == RESULT_OK) {
                    String imagePath;
                    Uri selectedImage = data.getData();
                    imagePath = selectedImage.getPath();
                    String path = Util.getPath(this, selectedImage);
                    if (imagePath != null) {
                        uploadpic(path);

                    }
                }
                break;
            default:
                break;
        }
    }
    private void uploadpic(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                    toast("图片上传成功:" + bmobFile.getFileUrl());
                    dongtai.setImage(bmobFile.getFileUrl().toString());
                    img_img.setVisibility(View.VISIBLE);
                    ImageLoaderFactory.getLoader().load(img_img, dongtai.getImage(), R.mipmap.head,null);
                    txt_image.setVisibility(View.GONE);

                } else {
                    Toast.makeText(DongTaiActivity.this, "上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProgress(Integer value) {

            }
        });
    }

}
