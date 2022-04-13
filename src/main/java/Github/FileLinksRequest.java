/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Github;

import java.io.Serializable;

/**
 *
 * @author NoahS
 */
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
