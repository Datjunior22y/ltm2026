package org.example;

import UDP.Customer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class udp_object2 {

    private static final String SERVER = "36.50.135.242";
    private static final int PORT = 2209;

    private static final String STUDENT_CODE = "B22DCCN200";
    private static final String QUESTION_CODE = "HQx7FkxP";


    // =========================================================
    // 1. Chuẩn hóa tên
    //
    // Input:
    // nguyen van hai duong
    //
    // Output:
    // DUONG, Nguyen Van Hai
    // =========================================================
    public static String normalizeName(String name) {

        // Xóa khoảng trắng đầu/cuối
        // và gom nhiều khoảng trắng thành một
        name = name.trim().replaceAll("\\s+", " ");

        String[] words = name.split(" ");

        StringBuilder result = new StringBuilder();

        // Lấy tên cuối
        String lastName = words[words.length - 1].toUpperCase();

        result.append(lastName);
        result.append(", ");

        // Lấy họ + tên đệm
        for (int i = 0; i < words.length - 1; i++) {

            String word = words[i].toLowerCase();

            word = Character.toUpperCase(word.charAt(0))
                    + word.substring(1);

            if (i > 0) {
                result.append(" ");
            }

            result.append(word);
        }

        return result.toString();
    }


    // =========================================================
    // 2. Tạo username
    //
    // Input:
    // nguyen van hai duong
    //
    // Output:
    // nvhduong
    // =========================================================
    public static String createUserName(String name) {

        name = name.trim().replaceAll("\\s+", " ");

        String[] words = name.split(" ");

        StringBuilder username = new StringBuilder();

        // Lấy chữ cái đầu của họ và tên đệm
        for (int i = 0; i < words.length - 1; i++) {

            username.append(
                    Character.toLowerCase(words[i].charAt(0))
            );
        }

        // Lấy toàn bộ tên cuối
        username.append(
                words[words.length - 1].toLowerCase()
        );

        return username.toString();
    }


    // =========================================================
    // 3. Đổi ngày sinh
    //
    // Input:
    // 10-11-2012
    //
    // Output:
    // 11/10/2012
    //
    // mm-dd-yyyy -> dd/mm/yyyy
    // =========================================================
    public static String formatDate(String date) {

        String[] parts = date.split("-");

        String month = parts[0];
        String day = parts[1];
        String year = parts[2];

        return day + "/" + month + "/" + year;
    }


    // =========================================================
    // 4. Serialize Customer
    // Object -> byte[]
    // =========================================================
    public static byte[] serialize(Customer customer)
            throws Exception {

        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();

        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(customer);
        objectOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }


    // =========================================================
    // 5. Deserialize Customer
    // byte[] -> Object
    // =========================================================
    public static Customer deserialize(
            byte[] data,
            int offset,
            int length
    ) throws Exception {

        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(
                        data,
                        offset,
                        length
                );

        ObjectInputStream objectInputStream =
                new ObjectInputStream(byteArrayInputStream);

        return (Customer) objectInputStream.readObject();
    }


    // =========================================================
    // MAIN
    // =========================================================
    public static void main(String[] args) {

        try {

            DatagramSocket socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(SERVER);


            // =================================================
            // BƯỚC 1
            // Gửi:
            // ;B22DCCN200;HQx7FkxP
            // =================================================

            String request =
                    ";" + STUDENT_CODE + ";" + QUESTION_CODE;

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


            // =================================================
            // BƯỚC 2
            // Nhận dữ liệu từ server
            //
            // 8 byte đầu: requestId
            // Các byte còn lại: Customer
            // =================================================

            byte[] buffer = new byte[65507];

            DatagramPacket responsePacket =
                    new DatagramPacket(
                            buffer,
                            buffer.length
                    );

            socket.receive(responsePacket);

            int responseLength =
                    responsePacket.getLength();


            // Lấy 8 byte requestId
            String requestId =
                    new String(
                            buffer,
                            0,
                            8,
                            StandardCharsets.UTF_8
                    );

            System.out.println(
                    "Request ID: " + requestId
            );


            // Lấy Customer từ byte thứ 8
            Customer customer =
                    deserialize(
                            buffer,
                            8,
                            responseLength - 8
                    );

            System.out.println(
                    "Customer nhan duoc:"
            );

            System.out.println(customer);


            // =================================================
            // BƯỚC 3
            // Chuẩn hóa tên
            //
            // nguyen van hai duong
            // ->
            // DUONG, Nguyen Van Hai
            // =================================================

            String newName =
                    normalizeName(
                            customer.getName()
                    );

            customer.setName(newName);


            // =================================================
            // BƯỚC 4
            // Đổi ngày sinh
            //
            // 10-11-2012
            // ->
            // 11/10/2012
            // =================================================

            String newDate =
                    formatDate(
                            customer.getDayOfBirth()
                    );

            customer.setDayOfBirth(newDate);


            // =================================================
            // BƯỚC 5
            // Tạo username
            //
            // Dùng tên ban đầu để tạo username:
            //
            // nguyen van hai duong
            // ->
            // nvhduong
            // =================================================

            /*
             * Vì sau khi normalizeName(), name đã trở thành:
             *
             * DUONG, Nguyen Van Hai
             *
             * nên cần lấy lại phần tên theo định dạng mới
             * để tạo username.
             */

            String normalizedName =
                    customer.getName();

            String[] nameParts =
                    normalizedName.split(", ");

            String firstMiddleNames =
                    nameParts[1];

            String[] words =
                    firstMiddleNames.split(" ");

            String lastName =
                    nameParts[0].toLowerCase();

            StringBuilder username =
                    new StringBuilder();

            // Chữ cái đầu của họ + tên đệm
            for (String word : words) {

                username.append(
                        Character.toLowerCase(
                                word.charAt(0)
                        )
                );
            }

            // Tên cuối
            username.append(lastName);

            customer.setUserName(
                    username.toString()
            );


            // =================================================
            // Hiển thị Customer sau khi xử lý
            // =================================================

            System.out.println(
                    "\nCustomer sau khi xu ly:"
            );

            System.out.println(customer);


            // =================================================
            // BƯỚC 6
            // Serialize Customer
            // =================================================

            byte[] customerData =
                    serialize(customer);


            // =================================================
            // BƯỚC 7
            // Tạo packet:
            //
            // 8 byte requestId
            // +
            // Object Customer
            // =================================================

            byte[] requestIdData =
                    requestId.getBytes(
                            StandardCharsets.UTF_8
                    );

            byte[] sendData =
                    new byte[
                            8 + customerData.length
                            ];


            // Copy requestId vào 8 byte đầu
            System.arraycopy(
                    requestIdData,
                    0,
                    sendData,
                    0,
                    8
            );


            // Copy Customer vào phía sau
            System.arraycopy(
                    customerData,
                    0,
                    sendData,
                    8,
                    customerData.length
            );


            // =================================================
            // BƯỚC 8
            // Gửi lại server
            // =================================================

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            PORT
                    );

            socket.send(sendPacket);

            System.out.println(
                    "\nDa gui Customer da xu ly len server."
            );


            // =================================================
            // BƯỚC 9
            // Đóng socket
            // =================================================

            socket.close();

            System.out.println(
                    "Da dong socket."
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
