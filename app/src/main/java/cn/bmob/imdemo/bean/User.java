package cn.bmob.imdemo.bean;

import cn.bmob.imdemo.db.NewFriend;
import cn.bmob.v3.BmobUser;

/**
 * @project:User
 */
public class User extends BmobUser {

    private String avatar;
    private String nickname;
    private String signname;
    private boolean vip = false;
    public User() {
    }

    public User(NewFriend friend) {
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }
}
