package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import tech.altier.synchronizer.Main;

public class RemoteFiles {
    private DbxClientV2 client;

    public RemoteFiles() {
        client = Main.client;
        
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                listViewRemote.getItems().add(metadata.getName());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
    }
}
