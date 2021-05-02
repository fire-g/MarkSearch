package index.subject;


import index.annotation.Search;

import java.util.Objects;

/**
 * 常规索引类型，包含
 * @author HaoTian
 */
public class Normal extends Index {

    public Normal(){
        this.type="normal";
    }
    /**
     * 索引类型(存储、索引)
     */
    @Search(value = "type",store = true,index = true)
    private String type;

    /**
     * 文件url(不分词、索引、存储)
     */
    @Search(value = "url",store = true,index = true)
    private String url;

    @Search(value = "title",store = true,index = true,participle = true)
    private String title;

    /**
     *用于索引
     */
    @Search(value = "content",participle = true,index = true)
    private String content;

    /**
     * 简介(存储、索引)
     */
    @Search(value = "synopsis",participle = true,store = true,index = true)
    private String synopsis;

    /**
     * (不分词、索引、存储)
     */
    @Search(value = "host",store = true,index = true)
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

    public final String getContent() {
        return content;
    }

    public final void setContent(String content) {
        this.content = content;
    }

    public final String getSynopsis() {
        return synopsis;
    }

    public final void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public final String getHost() {
        return host;
    }

    public final void setHost(String host) {
        this.host = host;
    }



    @Override
    public String toString() {
        return "Normal{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Normal)) {
            return false;
        }
        Normal normal = (Normal) o;
        return Objects.equals(getType(), normal.getType()) &&
                Objects.equals(getUrl(), normal.getUrl()) &&
                Objects.equals(getTitle(), normal.getTitle()) &&
                Objects.equals(getContent(), normal.getContent()) &&
                Objects.equals(getSynopsis(), normal.getSynopsis()) &&
                Objects.equals(getHost(), normal.getHost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getUrl(), getTitle(), getContent(), getSynopsis(), getHost());
    }
}
