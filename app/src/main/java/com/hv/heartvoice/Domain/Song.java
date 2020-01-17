package com.hv.heartvoice.Domain;

import static com.hv.heartvoice.Util.Constant.TYPE_SONG;

/**
 * 单曲
 */
public class Song extends BaseMultiItemEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String banner;

    /**
     * 音乐地址
     */
    private String uri;

    /**
     * 点击数
     */
    private int clicks_count;

    /**
     * 评论数
     */
    private int comments_count;

    /**
     * 歌词类型
     */
    private Integer style;

    /**
     * 歌词
     */
    private String lyric;

    /**
     * 创建该音乐的人
     */
    private User user;

    /**
     * 歌手
     */
    private User singer;

    /**
     * 播放总进度 单位毫秒
     */
    private long duration;

    /**
     * 播放进度
     */
    private long progress;


    @Override
    public int getItemType() {
        return TYPE_SONG;
    }

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(int clicks_count) {
        this.clicks_count = clicks_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSinger() {
        return singer;
    }

    public void setSinger(User singer) {
        this.singer = singer;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }
}
