package cn.bmob.imdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.model.UserModel;

/**启动界面
 * @project:SplashActivity
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class,null,true);
                }else{
                    startActivity(MainActivity.class,null,true);
                }
            }
        },1000);

    }
}
