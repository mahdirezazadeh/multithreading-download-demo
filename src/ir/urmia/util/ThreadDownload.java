package ir.urmia.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ThreadDownload extends Thread {
    private int threadId;
    private int startIndex;
    private int endIndex;
    private String address;
    private String fileName;
    private FileDownloader fileDownloader;

    //The constructor receives three parameters to initialize local variables: thread ID, start position and end position

    public ThreadDownload(int threadId, int startIndex, int endIndex, String address, String fileName, ThreadGroup threadGroup, FileDownloader fileDownloader) {
        super(threadGroup, address+threadId);
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.address = address;
        this.fileName = fileName;
        this.fileDownloader = fileDownloader;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            System.out.println("thread " + threadId + " Download start location " + startIndex + " End position:" + endIndex);

            //Set request header and request some resources to download
            conn.setRequestProperty("Range ", "bytes:" + startIndex + "-" + endIndex);
            //Server response code 206 indicates that some resources are successfully requested
            if (conn.getResponseCode() < 300) {
                InputStream inputStream = conn.getInputStream();
                RandomAccessFile raf = new RandomAccessFile(new File(fileName), "rw");
                //Find write start position
                raf.seek(startIndex);
                byte array[] = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(array)) != -1) {
                    raf.write(array, 0, len);
                }
                inputStream.close();
                raf.close();
                conn.disconnect();
                fileDownloader.done();
            } else {
                System.out.println("Server response code:" + conn.getResponseCode());
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
