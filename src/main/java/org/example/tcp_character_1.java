package org.example;

import java.io.*;
import java.net.Socket;

public class tcp_character_1 {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "B22DCCN200";
        String qCode = "uIWHcyMG";

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
                )
        ) {

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;

            out.write(request);
            out.newLine();
            out.flush();

            // b. Nhận danh sách tên miền từ server
            String data = in.readLine();

            System.out.println("Server gửi:");
            System.out.println(data);

            // c. Tìm các tên miền .edu
            StringBuilder result = new StringBuilder();

            String[] domains = data.split(",");

            for (String domain : domains) {

                domain = domain.trim();

                if (domain.endsWith(".edu")) {

                    if (result.length() > 0) {
                        result.append(", ");
                    }

                    result.append(domain);
                }
            }

            // Gửi kết quả về server
            out.write(result.toString());
            out.newLine();
            out.flush();

            System.out.println("Các tên miền .edu:");
            System.out.println(result);

            // d. Đóng kết nối
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}