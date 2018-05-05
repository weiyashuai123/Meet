package cn.bmob.imdemo.event;

import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * @project:ChatEvent
 */
public class ChatEvent {

    public BmobIMUserInfo info;

    public ChatEvent(BmobIMUserInfo info){
        this.info=info;
    }
}
