package org.example;


import java.io.*;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class tcp_character_2 {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "B22DCCN200";
        String qCode = "ofOLHvow";

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

            // b. Nhận chuỗi từ server
            String str = in.readLine();

            System.out.println("Chuỗi nhận được:");
            System.out.println(str);

            // c. Đếm số lần xuất hiện của các ký tự chữ hoặc số
            Map<Character, Integer> count = new LinkedHashMap<>();

            for (char c : str.toCharArray()) {

                // Chỉ xét chữ cái và chữ số
                if (Character.isLetterOrDigit(c)) {
                    count.put(c, count.getOrDefault(c, 0) + 1);
                }
            }

            // Tạo chuỗi kết quả
            StringBuilder result = new StringBuilder();

            for (Map.Entry<Character, Integer> entry : count.entrySet()) {

                if (entry.getValue() > 1) {
                    result.append(entry.getKey())
                            .append(":")
                            .append(entry.getValue())
                            .append(",");
                }
            }

            // Gửi kết quả về server
            out.write(result.toString());
            out.newLine();
            out.flush();

            System.out.println("Kết quả gửi:");
            System.out.println(result);

            // d. Đóng kết nối
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

