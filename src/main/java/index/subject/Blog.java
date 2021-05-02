package index.subject;


import index.annotation.Search;

import java.util.Date;
import java.util.Objects;

/**
 * @author HaoTian
 */
public class Blog extends Normal{

    public Blog(){
        this.setType("blog");
    }

    @Search(value = "author",store = true,index = true)
    private String author;

    @Search(value="post",store = true,index = true)
    private Date post;

    public final String getAuthor() {
        return author;
    }

    public final void setAuthor(String author) {
        this.author = author;
    }

    public final Date getPost() {
        if(post!=null) {
            return (Date) post.clone();
        }
        return null;
    }

    public final void setPost(Date post) {
        this.post = (Date)post.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Blog)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Blog blog = (Blog) o;
        return Objects.equals(getAuthor(), blog.getAuthor()) &&
                Objects.equals(getPost(), blog.getPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAuthor(), getPost());
    }

    @Override
    public String toString() {
        return "Blog{" +
                "author='" + author + '\'' +
                ", post=" + post +
                '}';
    }

}
