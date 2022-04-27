package Github;

import java.io.Serializable;

public class FileLinksRequest implements Serializable{
        protected String self;
        protected String git;
        protected String html;

    public String getSelf() {
        return self;
    }

    public String getGit() {
        return git;
    }

    public String getHtml() {
        return html;
    }

    @Override
    public String toString() {
        return "FileLinksRequest{" + "self=" + self + "\ngit=" + git + "\nhtml=" + html + '}';
    }
}
