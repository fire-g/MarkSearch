package index.subject;



import index.annotation.Search;

import java.io.Serializable;
import java.util.Date;

/**
 * 所有索引的父类
 * @author HaoTian
 */
public class Index implements Serializable {
    @Search(value = "score")
    private float score;

    @Search(value = "docId")
    private int docId;

    public Index(){
        this.time=new Date();
    }

    /**
     * 索引建立日期
     */
    @Search(value = "date",store = true,index = true)
    private Date time;

    public final float getScore() {
        return score;
    }

    public final void setScore(float score) {
        this.score = score;
    }

    public final int getDocId() {
        return docId;
    }

    public final void setDocId(int docId) {
        this.docId = docId;
    }

    public final Date getTime() {
        return (Date) time.clone();
    }

    public final void setTime(Date time) {
        this.time = (Date) time.clone();
    }
}
