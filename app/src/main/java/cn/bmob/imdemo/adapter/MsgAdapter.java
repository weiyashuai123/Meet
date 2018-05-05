package cn.bmob.imdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.Msg;

/**
 * Created by Administrator on 2017/3/20.
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> msgList;

    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        if (msg.getType() == Msg.TYPE_SEND) {
            holder.lefttxt.setText(msg.getContant());
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
        } else if (msg.getType() == Msg.TYPE_RECEIVE) {
            holder.righttxt.setText(msg.getContant());
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView lefttxt;
        TextView righttxt;

        public ViewHolder(View view) {

            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.ll_msg_left);
            rightLayout = (LinearLayout) view.findViewById(R.id.ll_msg_right);
            lefttxt = (TextView) view.findViewById(R.id.txt_msg_left);
            righttxt = (TextView) view.findViewById(R.id.txt_msg_right);
        }
    }
}
