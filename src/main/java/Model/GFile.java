package Model;

import Github.FileRequest;
import java.util.HashMap;
import java.util.Map;

public class GFile extends FileRequest{
    public GFile(FileRequest fr){
        this.copy(fr);
    }
}
