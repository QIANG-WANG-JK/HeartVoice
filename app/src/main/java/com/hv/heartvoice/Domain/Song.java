package com.hv.heartvoice.Domain;

import static com.hv.heartvoice.Util.Constant.TYPE_SONG;

/**
 * 单曲
 */
public class Song extends BaseMultiItemEntity {

    /**
     * 单曲
     * @return
     */
    @Override
    public int getItemType() {
        return TYPE_SONG;
    }

}
