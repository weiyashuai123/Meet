package cn.bmob.imdemo.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.base.ImageLoaderFactory;
import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.BmobUser;


public class MineActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appbar;
    private ImageView img_back;

    TextView txt_title;
    @Bind(R.id.nickname_mine)
    TextView txt_nickname;
    @Bind(R.id.username_mine)
    TextView txt_username;
    @Bind(R.id.email_mine)
    TextView txt_email;
    @Bind(R.id.signname_mine)
    TextView txt_signname;
    @Bind(R.id.level_mine)
    TextView txt_level;
    @Bind(R.id.location_mine)
    TextView txt_location;

    @Bind(R.id.img_mine_rz)
    ImageView img_rz;

    @Bind(R.id.txt_change_mine)
    TextView txt_change;
    @Bind(R.id.flactb_mine)
    FloatingActionButton img_bigIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);



    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
        initAction();
    }

    private void initViews() {
        appbar = (AppBarLayout) findViewById(R.id.appBar_mine);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_mine);
        img_back = (ImageView) findViewById(R.id.img_mine_back);
        txt_title = (TextView) findViewById(R.id.txt_title_mine);
        String ava = BmobUser.getCurrentUser(User.class).getAvatar();
        ImageLoaderFactory.getLoader().loadAvator(img_bigIcon, ava, R.mipmap.head);
        User user = BmobUser.getCurrentUser(User.class);
        txt_email.setText(user.getEmail());
        txt_level.setText("lv.16");
        txt_nickname.setText(user.getNickname());
        txt_username.setText(user.getUsername());
        txt_signname.setText(user.getSignname());
        txt_location.setText("中国");
        if (user.getEmailVerified()) {
            img_rz.setImageResource(R.drawable.r1);
        }
    }

    private void initAction() {
        appbar.addOnOffsetChangedListener(
                new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (verticalOffset == 0) {
                            txt_title.setText("");
                            //修改状态标记为展开
                        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                            txt_title.setText("个人资料");
                            //修改状态标记为折叠
                        } else {
                            txt_title.setText("");
                            //修改状态标记为中间
                        }
                    }
                }
        );
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txt_change.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startMeetActivity(ChangeInfoActivity.class);
                    }
                }

        );
    }
}
