package org.example;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udp_string2 {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int serverPort = 2208;

        String studentCode = "B22DCCN200";
        String qCode = "mRcsApDt";

        try {
            // =========================================
            // 1. Tạo UDP socket
            // =========================================
            DatagramSocket socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(serverIP);

            // =========================================
            // 2. Gửi mã sinh viên + mã câu hỏi
            // Format:
            // ;studentCode;qCode
            // =========================================

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

            // =========================================
            // 3. Nhận dữ liệu từ server
            // Format:
            // requestId;data
            // =========================================

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

            // =========================================
            // 4. Tách requestId và data
            // =========================================

            String[] parts =
                    response.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            System.out.println("Request ID: " + requestId);
            System.out.println("Data gốc: " + data);

            // =========================================
            // 5. Chuẩn hóa chuỗi
            // =========================================

            String[] words = data.trim().split("\\s+");

            StringBuilder normalized =
                    new StringBuilder();

            for (int i = 0; i < words.length; i++) {

                String word = words[i];

                // Đưa toàn bộ từ về chữ thường
                word = word.toLowerCase();

                // Viết hoa ký tự đầu tiên
                word = Character.toUpperCase(
                        word.charAt(0)
                ) + word.substring(1);

                normalized.append(word);

                // Thêm khoảng trắng giữa các từ
                if (i < words.length - 1) {
                    normalized.append(" ");
                }
            }

            String resultData =
                    normalized.toString();

            System.out.println(
                    "Chuỗi sau khi chuẩn hóa: "
                            + resultData
            );

            // =========================================
            // 6. Tạo thông điệp gửi về server
            // Format:
            // requestId;data
            // =========================================

            String answer =
                    requestId + ";" + resultData;

            System.out.println(
                    "Kết quả gửi: " + answer
            );

            // =========================================
            // 7. Gửi kết quả
            // =========================================

            byte[] answerData =
                    answer.getBytes("UTF-8");

            DatagramPacket answerPacket =
                    new DatagramPacket(
                            answerData,
                            answerData.length,
                            receivePacket.getAddress(),
                            receivePacket.getPort()
                    );

            socket.send(answerPacket);

            // =========================================
            // 8. Đóng socket
            // =========================================

            socket.close();

            System.out.println("Đã đóng socket.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

