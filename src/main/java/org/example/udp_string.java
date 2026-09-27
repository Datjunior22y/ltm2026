package org.example;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udp_string {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int serverPort = 2208;

        String studentCode = "B22DCCN200";
        String qCode = "og08BQex";

        try {
            // 1. Tạo UDP socket
            DatagramSocket socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(serverIP);

            // 2. Gửi mã sinh viên + mã câu hỏi
            String request =
                    ";" + studentCode + ";" + qCode;

            byte[] sendData =
                    request.getBytes("UTF-8");

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            serverPort
                    );

            socket.send(sendPacket);

            System.out.println("Đã gửi: " + request);

            // 3. Nhận dữ liệu từ server
            byte[] receiveData = new byte[4096];

            DatagramPacket receivePacket =
                    new DatagramPacket(
                            receiveData,
                            receiveData.length
                    );

            socket.receive(receivePacket);

            String response =
                    new String(
                            receivePacket.getData(),
                            0,
                            receivePacket.getLength(),
                            "UTF-8"
                    );

            System.out.println("Server gửi: " + response);

            // 4. Tách requestId và data
            String[] parts = response.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            // 5. Đếm số lần xuất hiện của từng ký tự
            int[] count = new int[65536];

            for (int i = 0; i < data.length(); i++) {

                char c = data.charAt(i);

                count[c]++;
            }

            // 6. Tìm ký tự xuất hiện nhiều nhất
            char maxChar = data.charAt(0);
            int maxCount = count[maxChar];

            for (int i = 1; i < data.length(); i++) {

                char c = data.charAt(i);

                if (count[c] > maxCount) {
                    maxCount = count[c];
                    maxChar = c;
                }
            }

            // 7. Tạo kết quả
            StringBuilder answer =
                    new StringBuilder();

            answer.append(requestId);
            answer.append(";");
            answer.append(maxChar);
            answer.append(":");

            // 8. Tìm vị trí ký tự
            // QUAN TRỌNG: vị trí bắt đầu từ 1
            for (int i = 0; i < data.length(); i++) {

                if (data.charAt(i) == maxChar) {

                    answer.append(i + 1);
                    answer.append(",");
                }
            }

            // 9. Gửi kết quả
            String result = answer.toString();

            System.out.println("Kết quả: " + result);

            byte[] answerData =
                    result.getBytes("UTF-8");

            DatagramPacket answerPacket =
                    new DatagramPacket(
                            answerData,
                            answerData.length,
                            receivePacket.getAddress(),
                            receivePacket.getPort()
                    );

            socket.send(answerPacket);

            // 10. Đóng socket
            socket.close();

            System.out.println("Đã đóng socket.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}