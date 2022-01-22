package ir.urmia;

import java.io.File;

public class AppContext {
    private static final int theadNumbers;
    private static final int poolSize;
    private static final String downloadPath;

    static {
        theadNumbers = 5;
        poolSize = 15;
        downloadPath = "downloads/";
        File directory = new File(downloadPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

    }

    public static int getTheadNumbers() {
        return theadNumbers;
    }

    public static int getPoolSize() {
        return poolSize;
    }

    public static String getDownloadPath() {
        return downloadPath;
    }
}
