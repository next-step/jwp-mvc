package next.reflection;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
public class Question {

    private long questionId;
    private String writer;
    private String title;
    private String contents;
    private Date createdDate;
    private int countOfComment;

    public Question(String writer, String title, String contents) {
        this(0, writer, title, contents, new Date(), 0);
    }

    public Question(long questionId, String writer, String title, String contents, Date createdDate, int countOfComment) {
        this.questionId = questionId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.countOfComment = countOfComment;
    }

    public long getTimeFromCreateDate() {
        return this.createdDate.getTime();
    }

    public void update(Question newQuestion) {
        this.title = newQuestion.title;
        this.contents = newQuestion.contents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (questionId ^ (questionId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Question other = (Question) obj;
        if (questionId != other.questionId)
            return false;
        return true;
    }
}
