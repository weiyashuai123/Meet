package cn.bmob.imdemo.bean;

/**
 * Created by Administrator on 2017/3/20.
 */
public class Msg {
    public static final int TYPE_RECEIVE = 0;
    public static final int TYPE_SEND = 1;
    private String contant;
    private User who;
    private int type;

    public Msg(String contant, int type, User who) {
        this.contant = contant;
        this.who = who;
        this.type = type;
    }
    public User getWho(){return who;}
    public String getContant(){
        return contant;
    }
    public int getType(){
        return type;
    }

    public void setContant(String contant) {
        this.contant = contant;
    }
}
