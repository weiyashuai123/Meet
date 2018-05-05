package cn.bmob.imdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.ImageLoaderFactory;
import cn.bmob.imdemo.bean.DongTai;

/**
 * Created by Administrator on 2017/5/27.
 */
public class DongTaiAdapter extends RecyclerView.Adapter<DongTaiAdapter.ViewHolder> {
    private List<DongTai> dongtaiList;

    public DongTaiAdapter(List<DongTai> dongtaiList) {
        this.dongtaiList = dongtaiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongtai, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DongTai dongTai = dongtaiList.get(position);
        holder.datetxt.setText(dongTai.getCreatedAt().toString());

        ImageLoaderFactory.getLoader().loadAvator(holder.avatar, dongTai.getAvatar(), R.mipmap.head);

        if (dongTai.isHasImage()) {
            holder.image.setVisibility(View.VISIBLE);
            ImageLoaderFactory.getLoader().load(holder.image, dongTai.getImage(), R.mipmap.head, null);
        }

        holder.contanttxt.setText(dongTai.getContant());
        holder.nametxt.setText(dongTai.getName());

    }

    @Override
    public int getItemCount() {
        return dongtaiList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View thisView;
        ImageView avatar;
        ImageView vip;
        ImageView image;
        TextView nametxt;
        TextView contanttxt;
        TextView datetxt;


        public ViewHolder(View view) {

            super(view);
            thisView = view;
//            vip = (ImageView) view.findViewById(R.id.img_vip_meet);
            avatar = (ImageView) view.findViewById(R.id.avatar_dongtai_item);
            image = (ImageView) view.findViewById(R.id.img_dongtai_item);
            nametxt = (TextView) view.findViewById(R.id.txt_nickname_dongtai_item);
            contanttxt = (TextView) view.findViewById(R.id.txt_contact_dongtai_item);
            datetxt = (TextView) view.findViewById(R.id.txt_time_dongtai_item);

        }
    }
}
