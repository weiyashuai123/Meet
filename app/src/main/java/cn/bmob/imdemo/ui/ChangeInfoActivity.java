package cn.bmob.imdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeInfoActivity extends BaseActivity {

    @Bind(R.id.et_nickname_change)
    EditText edt_nickname;
    @Bind(R.id.et_birthday_change)
    EditText edt_birthday;
    @Bind(R.id.et_phone_change)
    EditText edt_phone;
    @Bind(R.id.et_sex_change)
    EditText edt_sex;
    @Bind(R.id.et_signname_change)
    EditText edt_signname;
    @Bind(R.id.img_change_back)
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        initData();
        initAction();
    }

    private void initAction() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFinish();
            }
        });
    }

    private void initData() {
        User user = BmobUser.getCurrentUser(User.class);
        edt_nickname.setText(user.getNickname());
        edt_phone.setText(user.getMobilePhoneNumber());
        edt_sex.setText("男");
        edt_signname.setText(user.getSignname());
        edt_birthday.setText("1995.12.24");

    }

    private void changeFinish(){
        User user = BmobUser.getCurrentUser(User.class);
        user.setNickname(edt_nickname.getText().toString());
        user.setSignname(edt_signname.getText().toString());
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    finish();
                }
                else{
                    showError(e.getErrorCode());
                }
            }
        });

    }
}
