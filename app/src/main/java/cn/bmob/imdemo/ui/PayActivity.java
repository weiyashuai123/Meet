package cn.bmob.imdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import c.b.BP;
import c.b.PListener;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.BmobUser;

public class PayActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Button btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
        TextView txt_nickname, txt_state;
        ImageView img_vip, img_back;
        txt_nickname = (TextView) findViewById(R.id.txt_nickname_vip);
        txt_state = (TextView) findViewById(R.id.txt_state_vip);
        img_vip = (ImageView) findViewById(R.id.img_vip_vip);
        img_back = (ImageView) findViewById(R.id.img_back_vip);
        User user = BmobUser.getCurrentUser(User.class);
        txt_nickname.setText(user.getNickname());
        if (user.isVip()) {
            txt_state.setText("已开通会员");
            img_vip.setImageResource(R.drawable.vip);
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void pay() {
        BP.pay("MEET会员", "MEET会员支付测试", 0.02, true, new PListener() {
            @Override
            public void orderId(String s) {
                Toast.makeText(PayActivity.this, "订单号" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void succeed() {
                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(int i, String s) {
                if (i == 6001)
                    Toast.makeText(PayActivity.this, "支付失败，用户取消支付" + s, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PayActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unknow() {
                Toast.makeText(PayActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
