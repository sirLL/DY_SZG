package cn.dianyinhuoban.szg.bean;

import com.sunfusheng.marqueeview.IMarqueeItem;

public class CustomModel implements IMarqueeItem {
    private String title;
    private String content;
    private String url;

    @Override
    public CharSequence marqueeMessage() {
        return title;
    }

    public CustomModel(String message, String content, String url) {
        this.title = message;
        this.content = content;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
