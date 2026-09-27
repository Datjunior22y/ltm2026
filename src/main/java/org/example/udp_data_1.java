package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class udp_data_1 {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int serverPort = 2207;

        String studentCode = "B22DCCN200";
        String qCode = "Z2pgNquR";

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIP);

            // BƯỚC 1: Gửi mã sinh viên + mã câu hỏi
            String request = ";" + studentCode + ";" + qCode;

            byte[] sendData = request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket = new DatagramPacket(
                    sendData,
                    sendData.length,
                    serverAddress,
                    serverPort
            );

            socket.send(sendPacket);

            System.out.println("Đã gửi: " + request);

            // BƯỚC 2: Nhận dữ liệu từ server

            byte[] receiveData = new byte[4096];

            DatagramPacket receivePacket = new DatagramPacket(
                    receiveData,
                    receiveData.length
            );

            socket.receive(receivePacket);

            String response = new String(
                    receivePacket.getData(),
                    0,
                    receivePacket.getLength(),
                    StandardCharsets.UTF_8
            );

            System.out.println("Server gửi: " + response);

            // BƯỚC 3: Phân tích dữ liệu

            String[] parts = response.split(";");

            String requestId = parts[0];
            int n = Integer.parseInt(parts[1]);

            String[] numbers = parts[2].split(",");

            // BƯỚC 4: Đánh dấu các số đã xuất hiện
            boolean[] appeared = new boolean[n + 1];

            for (String number : numbers) {

                int value = Integer.parseInt(number);

                if (value >= 1 && value <= n) {
                    appeared[value] = true;
                }
            }

            // BƯỚC 5: Tìm các số còn thiếu

            List<Integer> missing = new ArrayList<>();

            for (int i = 1; i <= n; i++) {

                if (!appeared[i]) {
                    missing.add(i);
                }
            }
            // BƯỚC 6: Tạo chuỗi kết quả
            StringBuilder result = new StringBuilder();

            result.append(requestId);
            result.append(";");

            for (int i = 0; i < missing.size(); i++) {

                result.append(missing.get(i));

                if (i < missing.size() - 1) {
                    result.append(",");
                }
            }

            String answer = result.toString();

            // BƯỚC 7: Gửi kết quả về server

            byte[] answerData = answer.getBytes(StandardCharsets.UTF_8);

            DatagramPacket answerPacket = new DatagramPacket(
                    answerData,
                    answerData.length,
                    receivePacket.getAddress(),
                    receivePacket.getPort()
            );

            socket.send(answerPacket);

            System.out.println("Đã gửi kết quả: " + answer);

            // BƯỚC 8: Đóng socket

            socket.close();

            System.out.println("Đã đóng socket.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}