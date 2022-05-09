package Model;

import Github.FileRequest;

public class GFile extends FileRequest{
    public GFile(FileRequest fr){
        this.copy(fr);
    }
}
