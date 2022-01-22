package ir.urmia.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class FileDownloader {

    private ThreadGroup threadGroup;
    private ExecutorService executor;
    private String fileName;
    private String address;
    private int threadCount;
    private boolean isPaused = false;
    private volatile int downloaded = 0;

    public FileDownloader(String fileName, String address, int threadCount, ExecutorService executorService) {
        this.executor = executorService;
        this.fileName = fileName;
        this.address = address;
        this.threadCount = threadCount;
    }

    public void done() {
        downloaded++;

        System.out.println("downloaded " + downloaded + "/" + threadCount);

        if (downloaded == threadCount) {
            System.out.println("Download Completed!");
        }
    }

    public synchronized void pause() {
        if (!isPaused) {
            threadGroup.suspend();
            isPaused = true;
        }
    }

    public synchronized void resume() {
        if (isPaused) {
            threadGroup.resume();
            isPaused = false;
        }
    }

    public void downloadFile() throws IOException {
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            //Get the total size of File Bytes
            int fileLength = conn.getContentLength();


            System.out.println("Total file size: " + fileLength / 1024 / 1024 + "MB, " + fileLength + "B");
            int code = conn.getResponseCode();
            if (code == 200) {
                //Allocate "average" download bytes per thread
                int blockSize = fileLength / threadCount;

                //Create a random access stream to locally occupy files
//                new RandomAccessFile(fileName, "rw").close();

                //Important: assume that the file is only 10 bytes, download it in 3 threads, and understand the for loop code

                threadGroup = new ThreadGroup(fileName);

                for (int threadId = 0; threadId < threadCount; threadId++) {
                    int startIndex = threadId * blockSize;
                    int endIndex = (threadId + 1) * blockSize - 1;
                    if (threadId == threadCount - 1) {
                        endIndex = fileLength - 1;
                    }
                    //Once per cycle, start a thread to perform the download. Do not miss start().
                    ThreadDownload threadDownload = new ThreadDownload(threadId, startIndex, endIndex, address, fileName, threadGroup, this);
//                    threadDownload.start();
                    executor.execute(threadDownload);
                }
                conn.disconnect();
            } else {
                System.out.println("Request failed, server response code:" + code);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
