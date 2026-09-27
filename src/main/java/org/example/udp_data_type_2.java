package org.example;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udp_data_type_2 {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int serverPort = 2207;

        String studentCode = "B22DCCN200";
        String qCode = "IuQXxZUu";

        try {
            // ==============================
            // 1. Tạo UDP Socket
            // ==============================
            DatagramSocket socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(serverIP);

            // ==============================
            // 2. Gửi mã sinh viên + mã câu hỏi
            // Format:
            // ;studentCode;qCode
            // ==============================

            String request =
                    ";" + studentCode + ";" + qCode;

            byte[] sendData =
                    request.getBytes();

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            serverPort
                    );

            socket.send(sendPacket);

            System.out.println("Đã gửi: " + request);

            // ==============================
            // 3. Nhận dữ liệu từ server
            // Format:
            // requestId;a1,a2,...,a50
            // ==============================

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
                            receivePacket.getLength()
                    );

            System.out.println("Server gửi: " + response);

            // ==============================
            // 4. Tách requestId và danh sách số
            // ==============================

            String[] parts = response.split(";");

            String requestId = parts[0];

            String[] numberStrings =
                    parts[1].split(",");

            // ==============================
            // 5. Tìm MAX và MIN
            // ==============================

            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (String s : numberStrings) {

                int value = Integer.parseInt(s.trim());

                if (value > max) {
                    max = value;
                }

                if (value < min) {
                    min = value;
                }
            }

            System.out.println("Max = " + max);
            System.out.println("Min = " + min);

            // ==============================
            // 6. Tạo kết quả
            // Format:
            // requestId;max,min
            // ==============================

            String answer =
                    requestId + ";" + max + "," + min;

            // ==============================
            // 7. Gửi kết quả về server
            // ==============================

            byte[] answerData =
                    answer.getBytes();

            DatagramPacket answerPacket =
                    new DatagramPacket(
                            answerData,
                            answerData.length,
                            receivePacket.getAddress(),
                            receivePacket.getPort()
                    );

            socket.send(answerPacket);

            System.out.println(
                    "Đã gửi kết quả: " + answer
            );

            // ==============================
            // 8. Đóng socket
            // ==============================

            socket.close();

            System.out.println("Đã đóng socket.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

