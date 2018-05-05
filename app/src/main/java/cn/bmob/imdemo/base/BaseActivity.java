package cn.bmob.imdemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import cn.bmob.imdemo.Config;

/**基类
 * @project:BaseActivity
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Subscribe
    public void onEvent(Boolean empty){

    }

    protected void initView() {}

    protected void runOnMain(Runnable runnable) {
        runOnUiThread(runnable);
    }

    protected final static String NULL = "";
    private Toast toast;
    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(BaseActivity.this, NULL,Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shortToast(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void longToast(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void startMeetActivity(Class cla){
        Intent intent = new Intent(this,cla);
        startActivity(intent);
    }

    public void showError(int id) {
        switch (id) {
            case Settings.ERROR_NAME_OR_PASS:
                shortToast("用户名或密码不正确\n如果您忘记了密码请前往注册界面点击右上角?获取帮助");
                break;
            case Settings.CLASS_NOT_FOUND:
                shortToast("关联的class不存在");
                break;
            case Settings.EMPTY_USERNAME:
            case Settings.EMPTY_PASSWORD:
                shortToast("必须填写用户名和密码");
                break;
            case Settings.EMAIL_USED:
                shortToast("该邮箱已被注册");
                break;
            case Settings.EMPTY_EMAIL:
                shortToast("必须提供一个邮箱地址");
                break;
            case Settings.USER_NOT_FOUND:
                shortToast("没有找到该用户");
                break;
            case Settings.CANT_DO_THIS:
                shortToast("您没有更新此信息的权限");
                break;
            case Settings.ERROR_YZCODE:
                shortToast("验证码错误");
                break;
            case Settings.ERROR_AUTHDATA:
                shortToast("authData错误或已经绑定了其他用户账户");
                break;
            case Settings.PHONE_USED:
                shortToast("该手机号码已被注册");
                break;
            case Settings.ERROR_OLD_PASS:
                shortToast("您输入的旧密码不正确");
                break;
            case Settings.ERROR_FORMAT:
                shortToast("信息格式错误,请检查您的邮箱格式");
                break;
            case Settings.ONLY_VALUE:
                shortToast("唯一键不能存在重复的值");
                break;
            case 1002:
            case 1003:
            case 1004:
            case 1005:
            case 1006:
            case 1007:
                shortToast("服务停止：表中数据或服务器请求已达上限");
                break;
            case 1500:
                shortToast("上传文件已超出限制");
                break;
            case 9001:
                shortToast("Application Id为空，请初始化");
                break;
            case 9002:
                shortToast("解析返回数据出错");
                break;
            case 9003:
                shortToast("上传文件出错");
                break;
            case 9004:
                shortToast("文件上传失败");
                break;
            case 9005:
                shortToast("批量操作只支持最多50条");
                break;
            case 9006:
                shortToast("objectId为空");
                break;
            case 9007:
                shortToast("文件大小超过10M");
                break;
            case 9008:
                shortToast("上传文件不存在");
                break;
            case 9009:
                shortToast("没有缓存数据");
                break;
            case 9010:
                shortToast("网络超时");
                break;
            case 9011:
                shortToast("BmobUser类不支持批量操作");
                break;
            case 9012:
                shortToast("上下文为空");
                break;
            case 9013:
                shortToast("BmobObject格式不正确");
                break;
            case 9014:
                shortToast("第三方账号授权失败");
                break;
            case 9015:
                shortToast("其他错误");
                break;
            case 9016:
                shortToast("无网络连接，请检查您的手机网络");
                break;
            case 9017:
                shortToast("第三方登录错误");
                break;
            case 9018:
                shortToast("参数不能为空");
                break;
            case 9019:
                shortToast("手机号码/邮箱地址/验证码 格式错误");
                break;
            default:
                shortToast("其他错误：" + "错误id为" + id);
                break;
        }
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle,boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**隐藏软键盘-一般是EditText.getWindowToken()
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**Log日志
     * @param msg
     */
    public void log(String msg){
        if(Config.DEBUG){
            Logger.i(msg);
        }
    }

}
