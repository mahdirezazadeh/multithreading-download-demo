package ir.urmia;

import ir.urmia.util.FileDownloader;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class DownloadApp {
    public static void main(String[] args) {
        ExecutorService executorService = newFixedThreadPool(AppContext.getPoolSize());

        Scanner in = new Scanner(System.in);

        getFileDownloader(in, executorService);

        while (true){
            System.out.println("Download Another File(Y/N):");
            if(in.next().toLowerCase().charAt(0)=='y')
                getFileDownloader(in, executorService);
            else {
                System.out.println("""
                        By exiting this app incomplete downloads will store improperly.
                        Are you sure?(Y/N)""");
                if(in.next().toLowerCase().charAt(0)=='y') {
                    executorService.shutdown();
                    break;
                }
            }
        }


    }

    private static FileDownloader getFileDownloader(Scanner in, ExecutorService executorService) {
        System.out.println("Enter FIle URL:");
        String address = in.nextLine();

        System.out.println("Enter File Name:");
        String fileName = AppContext.getDownloadPath() + in.nextLine();

        FileDownloader fileDownloader = new FileDownloader(fileName, address, AppContext.getTheadNumbers(), executorService);
        try {
            fileDownloader.downloadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WhileMark:
        while (true) {
            System.out.println("""
                    0.return
                    1.Pause
                    2.Resume
                    Enter Your choice:
                    """);
            int option = in.nextInt();
            switch (option) {
                case 0:
                    break WhileMark;
                case 1:
                    fileDownloader.pause();
                    break;
                case 2:
                    fileDownloader.resume();
                    break;
                default:
            }
        }

        return fileDownloader;
    }
}
