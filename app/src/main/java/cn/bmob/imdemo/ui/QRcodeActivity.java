package cn.bmob.imdemo.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.base.ParentWithNaviActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class QRcodeActivity extends ParentWithNaviActivity {

    @Bind(R.id.txt_username_qrcode)
    TextView txt_username;
    @Bind(R.id.txt_nickname_qrcode)
    TextView txt_nickname;
    @Bind(R.id.img_avatar_qrcode)
    ImageView img_avatar;
    @Bind(R.id.img_code_qrcode)
    ImageView img_code;

    @Override
    protected String title() {
        return "二维码名片";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initNaviView();
        initData();
    }

    private void initData() {
        User me = BmobUser.getCurrentUser(User.class);
        txt_nickname.setText(me.getNickname());
        txt_username.setText(me.getUsername());
//        img_avatar.setImageResource();
        String username = me.getUsername();
        String codeValue = "Meet://add:"+username;
        Bitmap qrcode = EncodingUtils.createQRCode(codeValue,500,500, BitmapFactory.decodeResource(getResources(),R.mipmap.ic_meet_luncher));
        img_code.setImageBitmap(qrcode);
    }
}
