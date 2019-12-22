package com.hv.heartvoice.Domain;

import static com.hv.heartvoice.Util.Constant.TYPE_TITLE;

public class Title extends BaseMultiItemEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 构造方法
     * @param title
     */
    public Title(String title) {
        this.title = title;
    }

    public Title() {
    }

    /**
     * 标题
     * @return
     */
    @Override
    public int getItemType() {
        return TYPE_TITLE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
