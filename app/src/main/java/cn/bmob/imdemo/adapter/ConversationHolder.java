package cn.bmob.imdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.util.TimeUtil;
import cn.bmob.imdemo.util.ViewUtil;
import cn.bmob.imdemo.view.DragBubbleView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ConversationHolder extends BaseViewHolder {

  @Bind(R.id.iv_recent_avatar)
  public ImageView iv_recent_avatar;
  @Bind(R.id.tv_recent_name)
  public TextView tv_recent_name;
  @Bind(R.id.tv_recent_msg)
  public TextView tv_recent_msg;
  @Bind(R.id.tv_recent_time)
  public TextView tv_recent_time;
  @Bind(R.id.drag_num_meet)
  public TextView drag_num_meet;

  public ConversationHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_conversation,onRecyclerViewListener);
  }

  @Override
  public void bindData(Object o) {
      BmobIMConversation conversation =(BmobIMConversation)o;
      List<BmobIMMessage> msgs =conversation.getMessages();
      if(msgs!=null && msgs.size()>0){
          BmobIMMessage lastMsg =msgs.get(0);
          String content =lastMsg.getContent();
          if(lastMsg.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
              tv_recent_msg.setText(content);
          }else if(lastMsg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
              tv_recent_msg.setText("[图片]");
          }else if(lastMsg.getMsgType().equals(BmobIMMessageType.VOICE.getType())){
              tv_recent_msg.setText("[语音]");
          }else if(lastMsg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())){
              tv_recent_msg.setText("[位置]"+content);
          }else{//开发者自定义的消息类型，需要自行处理
              tv_recent_msg.setText("[未知]");
          }
          tv_recent_time.setText(TimeUtil.getChatTime(false, lastMsg.getCreateTime()));
      }
      //会话图标
      ViewUtil.setAvatar(conversation.getConversationIcon(), R.mipmap.head, iv_recent_avatar);
      //会话标题
      tv_recent_name.setText(conversation.getConversationTitle());
      String name = conversation.getConversationTitle();
      BmobQuery<User> query = new BmobQuery<>();
      query.addWhereEqualTo("username",name);
      query.findObjects(new FindListener<User>() {
          @Override
          public void done(List<User> list, BmobException e) {
              if (e==null){
                  User user = list.get(0);
                  tv_recent_name.setText(user.getNickname());
              }
          }
      });

      //查询指定未读消息数
      long unread = BmobIM.getInstance().getUnReadCount(conversation.getConversationId());
      if(unread>0){
          drag_num_meet.setVisibility(View.VISIBLE);
          drag_num_meet.setText(String.valueOf(unread));
      }else{
          drag_num_meet.setVisibility(View.GONE);
      }
  }

}