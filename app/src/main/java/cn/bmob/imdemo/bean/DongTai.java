package cn.bmob.imdemo.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/27.
 */
public class DongTai extends BmobObject {
    private String avatar;//头像
    private String image;//图片
    private boolean hasImage;//图片
    private String contant;//内容
    private String name;//用户昵称
    private boolean isVip;
    private int agree;

    public DongTai() {
        hasImage = false;
        avatar = "";
        image = "";
        contant = "";
        name = "";
        isVip = false;
        agree = 0;
    }
    public DongTai(User user){
        avatar = user.getAvatar();
        isVip = user.isVip();
        name = user.getNickname();
        hasImage = false;
        image = "";
        contant = "";
        agree = 0;
    }
    public void setUser(User user){
        avatar = user.getAvatar();
        isVip = user.isVip();
        name = user.getNickname();

    }

    public void setAgree(int agree) {
        this.agree = agree;
    }

    public int getAgree() {
        return agree;
    }

    public void setContant(String contant) {
        this.contant = contant;
    }

    public String getContant() {
        return contant;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        if (hasImage)
        return image;
        else
            return null;
    }

    public void setImage(String image) {
        hasImage = true;
        this.image = image;
    }

    public boolean isHasImage() {
        return hasImage;
    }
}
