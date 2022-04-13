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
public class FileRequest implements Serializable{
    protected String type;
    protected int size;
    protected String name;
    protected String path;
    protected String sha;
    protected String url;
    protected String git_url;
    protected String html_url;
    protected String download_url;
    protected FileLinksRequest links;
    
    protected FileRequest(){
        type = "dir";
        size = 0;
        name = "root";
        path = "";
        sha = "";
        url = "";
        git_url = "";
        html_url = "";
        download_url = "";
        links = null;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getSha() {
        return sha;
    }

    public String getUrl() {
        return url;
    }

    public String getGit_url() {
        return git_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public FileLinksRequest getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return "FileRequest{" + "\ntype=" + type + "\nsize=" + size + "\nname=" + name + "\npath=" + path + "\nsha=" + sha + "\nurl=" + url + "\ngit_url=" + git_url + "\nhtml_url=" + html_url + "\ndownload_url=" + download_url + "\nlinks=" + links + "\n}";
    }
    
    public void copy(FileRequest fr){
        type = fr.getType();
        size = fr.getSize();
        name = fr.getName();
        path = fr.getPath();
        sha = fr.getSha();
        url = fr.getUrl();
        git_url = fr.getGit_url();
        html_url = fr.getHtml_url();
        download_url = fr.getDownload_url();
        links = fr.getLinks();
    }
}
