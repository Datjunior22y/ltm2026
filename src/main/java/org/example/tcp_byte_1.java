package org.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class tcp_byte_1 {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2206;

        String studentCode = "B22DCCN200";
        String qCode = "pZQwsu4q";

        try {
            Socket socket = new Socket(host, port);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // =========================================
            // a. Gửi mã sinh viên + mã câu hỏi
            // =========================================

            String request = studentCode + ";" + qCode;

            out.write(request.getBytes(StandardCharsets.UTF_8));
            out.flush();


            // =========================================
            // b. Nhận dữ liệu từ server
            // =========================================

            byte[] buffer = new byte[4096];

            int length = in.read(buffer);

            String data = new String(
                    buffer,
                    0,
                    length,
                    StandardCharsets.UTF_8
            ).trim();

            System.out.println("Input: " + data);


            // =========================================
            // Chuyển chuỗi thành mảng số nguyên
            // =========================================

            String[] arr = data.split(",");

            int[] numbers = new int[arr.length];

            for (int i = 0; i < arr.length; i++) {
                numbers[i] = Integer.parseInt(arr[i].trim());
            }


            // =========================================
            // c. Tìm khoảng cách nhỏ nhất
            // =========================================

            int minDistance = Integer.MAX_VALUE;
            int number1 = 0;
            int number2 = 0;

            for (int i = 0; i < numbers.length - 1; i++) {

                for (int j = i + 1; j < numbers.length; j++) {

                    int distance = Math.abs(numbers[i] - numbers[j]);

                    if (distance < minDistance) {
                        minDistance = distance;
                        number1 = numbers[i];
                        number2 = numbers[j];
                    }
                }
            }


            // =========================================
            // Tạo kết quả
            // =========================================

            String result =
                    minDistance + "," +
                            number1 + "," +
                            number2;

            System.out.println("Result: " + result);


            // =========================================
            // Gửi kết quả về server
            // =========================================

            out.write(result.getBytes(StandardCharsets.UTF_8));
            out.flush();


            // =========================================
            // d. Đóng kết nối
            // =========================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

