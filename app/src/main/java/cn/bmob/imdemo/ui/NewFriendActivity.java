package cn.bmob.imdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.adapter.ConversationAdapter;
import cn.bmob.imdemo.adapter.NewFriendAdapter;
import cn.bmob.imdemo.adapter.OnRecyclerViewListener;
import cn.bmob.imdemo.adapter.base.IMutlipleItem;
import cn.bmob.imdemo.base.ParentWithNaviActivity;
import cn.bmob.imdemo.bean.Conversation;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.db.NewFriend;
import cn.bmob.imdemo.db.NewFriendManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**新朋友
 * @project:NewFriendActivity
 */
public class NewFriendActivity extends ParentWithNaviActivity {

    @Bind(R.id.ll_root)
    LinearLayout ll_root;
    @Bind(R.id.rc_view)
    RecyclerView rc_view;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    NewFriendAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected String title() {
        return "新朋友";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                finish();
            }

            @Override
            public void clickRight() {
                popMenu();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversation);
        initNaviView();
        //单一布局
        IMutlipleItem<NewFriend> mutlipleItem = new IMutlipleItem<NewFriend>() {

            @Override
            public int getItemViewType(int postion, NewFriend c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_new_friend;
            }

            @Override
            public int getItemCount(List<NewFriend> list) {
                return list.size();
            }
        };
        ll_root.setBackgroundResource(R.drawable.bg_meet_mohu);
        adapter = new NewFriendAdapter(this,mutlipleItem,null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(this).updateBatchStatus();
        setListener();
    }

    private void setListener(){
        ll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                log("点击："+position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                NewFriendManager.getInstance(NewFriendActivity.this).deleteNewFriend(adapter.getItem(position));
                adapter.remove(position);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
      查询本地会话
     */
    public void query(){
        adapter.bindDatas(NewFriendManager.getInstance(this).getAllNewFriend());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }

    private void popMenu() {
        PopupMenu menu = new PopupMenu(this, getrightView());
        try {
            Field field = menu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(menu);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        getMenuInflater().inflate(R.menu.menu_pop, menu.getMenu());
        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.scan_pop:
//                                toast("click");
                                startActivityForResult(new Intent(NewFriendActivity.this, CaptureActivity.class), 0);
                                break;
                            case R.id.add_friend_pop:
                                startActivity(SearchUserActivity.class, null);
                                break;
                        }
                        return true;
                    }
                }
        );
        menu.show();
//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
                    intent.setClass(NewFriendActivity.this, UserInfoActivity.class);
                    intent.putExtra(NewFriendActivity.this.getPackageName(), bundle);
                    startActivity(intent);
                } else if (e != null) {
                    toast(e.getMessage());
                } else {
                    toast("查无此人");
                }
            }
        });
    }
}
