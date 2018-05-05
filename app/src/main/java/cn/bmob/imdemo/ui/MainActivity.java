package cn.bmob.imdemo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.base.ImageLoaderFactory;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.db.NewFriendManager;
import cn.bmob.imdemo.event.RefreshEvent;
import cn.bmob.imdemo.model.UserModel;
import cn.bmob.imdemo.ui.fragment.ContactFragment;
import cn.bmob.imdemo.ui.fragment.ConversationFragment;
import cn.bmob.imdemo.ui.fragment.OtherFragment;
import cn.bmob.imdemo.ui.fragment.SetFragment;
import cn.bmob.imdemo.util.CircularAnim;
import cn.bmob.imdemo.util.IMMLeaks;
import cn.bmob.imdemo.util.Util;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * @author :weiyashuai
 * @project:MainActivity
 */
public class MainActivity extends BaseActivity implements ObseverListener {

    @Bind(R.id.btn_conversation)
    Button btn_conversation;
    @Bind(R.id.btn_set)
    Button btn_set;

    @Bind(R.id.btn_contact)
    Button btn_contact;

    @Bind(R.id.iv_conversation_tips)
    ImageView iv_conversation_tips;

    @Bind(R.id.iv_contact_tips)
    ImageView iv_contact_tips;

    @Bind(R.id.txt_mContact_main)
    TextView txt_mDescription;
    @Bind(R.id.txt_nickname_main)
    TextView txt_nickname;
    @Bind(R.id.nav_view_main)
    NavigationView nav_main;

    @Bind(R.id.drawerLayout_main)
    DrawerLayout drawerLayout_main;
    @Bind(R.id.img_bigIcon_main)
    ImageView img_bigIcon;

    private Button[] mTabs;
    private ConversationFragment conversationFragment;
    private OtherFragment otherFragment;
    ContactFragment contactFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;

    private Uri imageUri;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenuAction();
        setdata();
        //connect server
        User user = BmobUser.getCurrentUser(User.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("成功连接");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                toast("" + status.getMsg());
            }
        });
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());
//        downloadAvatar();
        setAvatar();
    }

    private void downloadAvatar() {
        String url = BmobUser.getCurrentUser(User.class).getAvatar();
        if (!url.isEmpty()) {
            BmobFile myAvatar = new BmobFile(url.substring(7), "", url);
            myAvatar.download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        SharedPreferences.Editor spe = getSharedPreferences("user",MODE_PRIVATE).edit();
                        spe.putString("avatar",s);
                        spe.commit();
                        Bitmap bitmap = BitmapFactory.decodeFile(s);
                        img_bigIcon.setImageBitmap(bitmap);
                    } else {

                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
    }
    private void setAvatar(){
        String ava = BmobUser.getCurrentUser(User.class).getAvatar();
        ImageLoaderFactory.getLoader().loadAvator(img_bigIcon, ava, R.mipmap.head);
    }
    private void downloadAvatar(String url) {
        if (!url.isEmpty()) {
            BmobFile myAvatar = new BmobFile(url.substring(7), "", url);
            myAvatar.download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        SharedPreferences.Editor spe = getSharedPreferences("user",MODE_PRIVATE).edit();
                        spe.putString("avatar",s);
                        spe.commit();
                        Bitmap bitmap = BitmapFactory.decodeFile(s);
                        img_bigIcon.setImageBitmap(bitmap);
                    } else {

                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setdata();
    }

    private void setdata() {
        String nickname = BmobUser.getCurrentUser(User.class).getNickname();
        String signname = BmobUser.getCurrentUser(User.class).getSignname();
        txt_mDescription.setText(signname);
        txt_nickname.setText(nickname);
    }

    private void initMenuAction() {
        nav_main.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.code_menu:
                                startMeetActivity(QRcodeActivity.class);
                                break;
                            case R.id.mine_menu:
                                startMeetActivity(MineActivity.class);
                                break;
                            case R.id.fight_menu:
                                shortToast("战斗");
                                break;
                            case R.id.setting_menu:
                                shortToast("设置");
                                break;
                            case R.id.vip_menu:
                                startMeetActivity(PayActivity.class);
                                break;
                            case R.id.exit_menu:
                                UserModel.getInstance().logout();
                                //可断开连接
                                BmobIM.getInstance().disConnect();
                                MainActivity.this.finish();
                                startMeetActivity(LoginActivity.class);
                                break;
                        }
                        return true;
                    }
                }
        );
        img_bigIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    String imagePath;
                    Uri selectedImage = data.getData();
                    imagePath = selectedImage.getPath();
                    String path = Util.getPath(this, selectedImage);
                    if (imagePath != null) {
//                        toast(path+"");
//                        toast(uri);
//                        Bitmap bitmap = BitmapFactory.decodeFile(path);
//                        img_bigIcon.setImageBitmap(bitmap);
                        uploadpic(path);
                    }
                }
                break;
            case 999:
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String result = bundle.getString("result");
                    if (result.startsWith("Meet://add:")) {
                        String username = result.substring(11);
                        addFriend(username);
                    } else {
                        toast(result);
                    }

                }
                break;
            default:
                break;
        }

    }
    private void addFriend(String username) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    User user = list.get(0);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("u", user);
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, UserInfoActivity.class);
                    intent.putExtra(MainActivity.this.getPackageName(), bundle);
                    startActivity(intent);
                } else if (e != null) {
                    toast(e.getMessage());
                } else {
                    toast("查无此人");
                }
            }
        });
    }

    private void uploadpic(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                    toast("图片上传成功:" + bmobFile.getFileUrl());
                    User user = BmobUser.getCurrentUser(User.class);
                    user.setAvatar(bmobFile.getFileUrl().toString());
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
//                                downloadAvatar();
                                setAvatar();
                                toast("更新头像成功");
                            } else {
                                toast(e.getMessage());
                            }
                        }
                    });
                } else {
                    toast("上传失败：" + e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value) {

            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        mTabs = new Button[3];
        mTabs[0] = btn_conversation;
        mTabs[1] = btn_contact;
        mTabs[2] = btn_set;
        mTabs[0].setSelected(true);
        initTab();
    }

    private void initTab() {
        conversationFragment = new ConversationFragment();
        otherFragment = new OtherFragment();
        contactFragment = new ContactFragment();
        fragments = new Fragment[]{conversationFragment, contactFragment, otherFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, conversationFragment).
                add(R.id.fragment_container, contactFragment)
                .add(R.id.fragment_container, otherFragment)
                .hide(otherFragment).hide(contactFragment)
                .show(conversationFragment).commit();
    }

    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_contact:
                index = 1;
                break;
            case R.id.btn_set:
                index = 2;
                break;
        }
        onTabIndex(index);
    }

    private void onTabIndex(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示小红点
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        log("---主页接收到自定义消息---");
        checkRedPoint();
    }

    private void checkRedPoint() {
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
            iv_conversation_tips.setVisibility(View.VISIBLE);
        } else {
            iv_conversation_tips.setVisibility(View.GONE);
        }
        //是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            iv_contact_tips.setVisibility(View.VISIBLE);
        } else {
            iv_contact_tips.setVisibility(View.GONE);
        }
    }

    private void showMenu() {
        drawerLayout_main.openDrawer(GravityCompat.START);
    }

}
