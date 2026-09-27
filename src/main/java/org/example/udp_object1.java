package org.example;

import UDP.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class udp_object1 {

    static final String SERVER = "36.50.135.242";
    static final int PORT = 2209;

    static final String STUDENT_CODE = "B22DCCN200";
    static final String QUESTION_CODE = "IepfQ48M";

    // Chuẩn hóa họ tên:
    // nguyen van tuan nam
    // -> Nguyen Van Tuan Nam
    static String normalizeName(String name) {

        name = name.trim().replaceAll("\\s+", " ");

        String[] words = name.split(" ");

        StringBuilder result = new StringBuilder();

        for (String word : words) {

            if (word.length() == 0) {
                continue;
            }

            word = word.toLowerCase();

            String normalized =
                    Character.toUpperCase(word.charAt(0))
                            + word.substring(1);

            if (result.length() > 0) {
                result.append(" ");
            }

            result.append(normalized);
        }

        return result.toString();
    }

    // Tạo username:
    // Nguyen Van Tuan Nam
    // -> namnvt
    //
    // Lấy tên cuối cùng + chữ cái đầu của các từ phía trước.
    static String createUsername(String name) {

        String[] words = name.toLowerCase().split("\\s+");

        if (words.length == 0) {
            return "";
        }

        StringBuilder username = new StringBuilder();

        // Tên
        username.append(words[words.length - 1]);

        // Chữ cái đầu của họ và tên đệm
        for (int i = 0; i < words.length - 1; i++) {
            username.append(words[i].charAt(0));
        }

        return username.toString();
    }

    // Chuyển Student thành mảng byte để gửi qua UDP
    static byte[] serializeStudent(Student student) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(student);
        oos.flush();

        return bos.toByteArray();
    }

    // Chuyển mảng byte nhận được thành Student
    static Student deserializeStudent(byte[] data, int offset, int length)
            throws Exception {

        ByteArrayInputStream bis =
                new ByteArrayInputStream(data, offset, length);

        ObjectInputStream ois = new ObjectInputStream(bis);

        return (Student) ois.readObject();
    }

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress serverAddress =
                    InetAddress.getByName(SERVER);

            /*
             * a. Gửi:
             * ;B22DCCN200;IepfQ48M
             */
            String request = ";" + STUDENT_CODE + ";" + QUESTION_CODE;

            byte[] requestData =
                    request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket requestPacket =
                    new DatagramPacket(
                            requestData,
                            requestData.length,
                            serverAddress,
                            PORT
                    );

            socket.send(requestPacket);

            System.out.println("Da gui: " + request);

            /*
             * b. Nhận dữ liệu từ server.
             *
             * 8 byte đầu: requestId
             * Các byte còn lại: đối tượng Student
             */
            byte[] buffer = new byte[65507];

            DatagramPacket responsePacket =
                    new DatagramPacket(buffer, buffer.length);

            socket.receive(responsePacket);

            int length = responsePacket.getLength();

            // Lấy 8 byte đầu
            String requestId =
                    new String(
                            buffer,
                            0,
                            8,
                            StandardCharsets.UTF_8
                    );

            System.out.println("Request ID: " + requestId);

            // Deserialize Student từ byte thứ 8 trở đi
            Student student =
                    deserializeStudent(
                            buffer,
                            8,
                            length - 8
                    );

            System.out.println("Student nhan duoc:");
            System.out.println(student);

            /*
             * c1. Chuẩn hóa họ tên
             */
            String normalizedName =
                    normalizeName(student.getName());

            student.setName(normalizedName);

            /*
             * c2. Tạo email
             *
             * Ví dụ:
             * Nguyen Van Tuan Nam
             * -> namnvt@ptit.edu.vn
             */
            String username =
                    createUsername(student.getName());

            String email =
                    username + "@ptit.edu.vn";

            student.setEmail(email);

            System.out.println("Student sau khi xu ly:");
            System.out.println(student);

            /*
             * c3. Serialize Student đã sửa
             */
            byte[] studentData =
                    serializeStudent(student);

            /*
             * Tạo packet mới:
             *
             * 8 byte requestId
             * +
             * byte[] Student
             */
            byte[] requestIdData =
                    requestId.getBytes(StandardCharsets.UTF_8);

            byte[] sendData =
                    new byte[8 + studentData.length];

            // 8 byte đầu
            System.arraycopy(
                    requestIdData,
                    0,
                    sendData,
                    0,
                    8
            );

            // Student từ byte thứ 8
            System.arraycopy(
                    studentData,
                    0,
                    sendData,
                    8,
                    studentData.length
            );

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            PORT
                    );

            socket.send(sendPacket);

            System.out.println("Da gui Student da xu ly len server.");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
