package com.hv.heartvoice.Domain;

import static com.hv.heartvoice.Util.Constant.TYPE_TITLE;

public class Title extends BaseMultiItemEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 标题
     * @return
     */
    @Override
    public int getItemType() {
        return TYPE_TITLE;
    }

}
