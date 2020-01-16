package com.hv.heartvoice.Domain;

import com.hv.heartvoice.Util.LogUtil;

import java.util.List;

import static com.hv.heartvoice.Util.Constant.TYPE_SHEET;

public class Sheet extends BaseMultiItemEntity {

    /**
     * 歌单标题
     */
    private String title;

    /**
     * 歌单封面
     */
    private String banner;

    /**
     * 描述
     */
    private String description;

    /**
     * 点击数
     */
    private int clicks_count;

    /**
     * 收藏数
     */
    private int collection;

    /**
     * 收藏数
     */
    private int collections_count;

    /**
     * 评论数
     */
    private int comments_count;

    /**
     * 音乐数量
     */
    private int songs_count;

    /**
     * 歌单创建者
     */
    private User user;

    /**
     * 歌曲
     */
    private List<Song> songs;

    /**
     * 是否收藏
     * 如果有值代表收藏了
     */
    private Integer collection_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(int clicks_count) {
        this.clicks_count = clicks_count;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getSongs_count() {
        return songs_count;
    }

    public void setSongs_count(int songs_count) {
        this.songs_count = songs_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Integer getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Integer collection_id) {
        this.collection_id = collection_id;
    }

    public int getCollections_count() {
        return collections_count;
    }

    public void setCollections_count(int collections_count) {
        this.collections_count = collections_count;
    }

    /**
     * 是否收藏
     * @return
     */
    public boolean isCollection(){
        return collection_id != null;
    }

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
