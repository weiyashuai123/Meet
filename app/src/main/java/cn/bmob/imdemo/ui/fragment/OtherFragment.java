package cn.bmob.imdemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.adapter.DongTaiAdapter;
import cn.bmob.imdemo.base.ParentWithNaviActivity;
import cn.bmob.imdemo.base.ParentWithNaviFragment;
import cn.bmob.imdemo.bean.DongTai;
import cn.bmob.imdemo.ui.DongTaiActivity;
import cn.bmob.imdemo.ui.SearchUserActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/14.
 */
public class OtherFragment extends ParentWithNaviFragment {

    @Bind(R.id.swipe_refresh_other)
    SwipeRefreshLayout sw_refresh;
    @Bind(R.id.recycler_other_frag)
    RecyclerView recyclerView;
    DongTaiAdapter adapter;
    private List<DongTai> dongTaiList = new ArrayList<>();

    @Override
    protected String title() {
        return "动态";
    }

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public OtherFragment() {
    }

    @Override
    public Object left() {
        return R.drawable.m_menu;
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_other, container, false);
        initNaviView();

        initData();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initData() {

        query();
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                DrawerLayout drawerLayout_main = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout_main);
                drawerLayout_main.openDrawer(GravityCompat.START);
            }

            @Override
            public void clickRight() {
                popMenu();
            }
        };
    }

    private void popMenu() {
        PopupMenu menu = new PopupMenu(getActivity(), getrightView());
        try {
            Field field = menu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(menu);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        getActivity().getMenuInflater().inflate(R.menu.menu_dongtai, menu.getMenu());
        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_pop:
                                startActivity(DongTaiActivity.class, null);
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
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
    }

    @Override
    public void onPause() {
        super.onResume();
        sw_refresh.setRefreshing(false);

    }

    private void query() {
        BmobQuery<DongTai> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<DongTai>() {
            @Override
            public void done(List<DongTai> list, BmobException e) {
                if (e==null){
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new DongTaiAdapter(list);
                    recyclerView.setAdapter(adapter);
                    sw_refresh.setRefreshing(false);
                }
                else {
                    toast(e.getMessage());
                    sw_refresh.setRefreshing(false);
                }
            }
        });
    }
}
