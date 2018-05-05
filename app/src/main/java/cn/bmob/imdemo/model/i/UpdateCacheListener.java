package cn.bmob.imdemo.model.i;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

/**
 * @project:UpdateCacheListener
 */
public abstract class UpdateCacheListener extends BmobListener1 {
    public abstract void done(BmobException e);

    @Override
    protected void postDone(Object o, BmobException e) {
        done(e);
    }
}
