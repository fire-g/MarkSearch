package com.mark.search.index.subject;


import com.mark.search.index.annotation.Search;

import java.util.Objects;

/**
 * @author HaoTian
 */
public class Question extends Index {

    public Question() {
        this.type = "question";
    }

    /**
     * 索引类型(存储、索引)
     */
    @Search(value = "type", store = true)
    private String type;

    /**
     * 文件url(不分词、索引、存储)
     */
    @Search(value = "url", store = true, index = true)
    private String url;

    @Search(value = "title", participle = true, store = true, index = true)
    private String title;

    /**
     * (不分词、索引、存储)
     */
    @Search(value = "host", store = true, index = true)
    private String host;

    public final String getType() {
        return type;
    }

    public final void setType(String type) {
        this.type = type;
    }

    public final String getUrl() {
        return url;
    }

    public final void setUrl(String url) {
        this.url = url;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final String getHost() {
        return host;
    }

    public final void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "Question{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        Question question = (Question) o;
        return Objects.equals(getType(), question.getType()) &&
                Objects.equals(getUrl(), question.getUrl()) &&
                Objects.equals(getTitle(), question.getTitle()) &&
                Objects.equals(getHost(), question.getHost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getUrl(), getTitle(), getHost());
    }
}
