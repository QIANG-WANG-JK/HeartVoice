package com.hv.heartvoice.Domain;

import static com.hv.heartvoice.Util.Constant.TYPE_SHEET;

public class Sheet extends BaseMultiItemEntity {

    /**
     * 歌单
     * @return
     */
    @Override
    public int getItemType() {
        return TYPE_SHEET;
    }

    /**
     * 占多少列
     * @return
     */
    @Override
    public int getSpanSize() {
        return 1;
    }

}
