package tiny.mq.utility;

import java.io.File;

public class FolderAux {
    public static boolean createFolder(String folderName){
        File folder = new File(folderName);
        return folder.mkdir();
    }
}
