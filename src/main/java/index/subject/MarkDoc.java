package index.subject;

import java.io.Serializable;

/**
 * @author haotian
 */
public class MarkDoc implements Serializable {

    /**
     * 文档得分
     */
    public float score;

    /**
     * 文档id
     */
    public int doc;

    /**
     * 结点id
     */
    public int node;

    @Override
    public String toString() {
        return "MarkDoc{" +
                "score=" + score +
                ", doc=" + doc +
                ", node=" + node +
                '}';
    }
}
