package org.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class tcp_byte_2 {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2206;

        String studentCode = "B22DCCN200";
        String qCode = "kStDJqz3";

        try {
            Socket socket = new Socket(host, port);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + qCode;

            out.write(request.getBytes());
            out.flush();

            // b. Nhận dữ liệu từ server
            byte[] buffer = new byte[4096];

            int length = in.read(buffer);

            String data = new String(buffer, 0, length).trim();

            System.out.println("Input: " + data);

            // Chuyển thành mảng số
            String[] parts = data.split(",");

            int[] numbers = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i].trim());
            }

            // c. Tìm giá trị lớn thứ hai
            int max = Integer.MIN_VALUE;
            int secondMax = Integer.MIN_VALUE;

            for (int x : numbers) {
                if (x > max) {
                    secondMax = max;
                    max = x;
                } else if (x > secondMax && x < max) {
                    secondMax = x;
                }
            }

            // Tìm vị trí của secondMax
            int position = -1;

            for (int i = 0; i < numbers.length; i++) {
                if (numbers[i] == secondMax) {
                    position = i;
                    break;
                }
            }

            String result = secondMax + "," + position;

            System.out.println("Submitted: " + result);

            // Gửi kết quả
            out.write(result.getBytes());
            out.flush();

            // d. Đóng kết nối
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

