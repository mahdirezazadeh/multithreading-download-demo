package ir.urmia;

import ir.urmia.util.FileDownloader;

import java.io.IOException;
import java.util.Scanner;

public class DownloadApp {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter FIle URL:");
        String address = in.nextLine();

        System.out.println("Enter File Name:");
        String fileName = in.nextLine();

        FileDownloader fileDownloader = new FileDownloader(fileName, address,5);
        try {
            fileDownloader.downloadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WhileMark:
        while (true)
        {
            System.out.println("""
                    0.Exit
                    1.Pause
                    2.Resume
                    Enter Your choice:
                    """);
            int option = in.nextInt();
            switch (option){
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

    }
}
