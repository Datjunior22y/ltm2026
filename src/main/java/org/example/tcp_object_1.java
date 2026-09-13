package org.example;

import TCP.Laptop;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class tcp_object_1 {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "B22DCCN200";
        String qCode = "nCpXVakE";

        try {
            Socket socket = new Socket(host, port);

            // =========================================
            // Tạo ObjectOutputStream trước
            // =========================================

            ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream());

            out.flush();

            // =========================================
            // Tạo ObjectInputStream
            // =========================================

            ObjectInputStream in =
                    new ObjectInputStream(socket.getInputStream());


            // =========================================
            // 1. Gửi mã sinh viên + mã câu hỏi
            // =========================================

            String request = studentCode + ";" + qCode;

            out.writeObject(request);
            out.flush();


            // =========================================
            // 2. Nhận đối tượng Laptop
            // =========================================

            Laptop laptop = (Laptop) in.readObject();

            System.out.println("Laptop nhận được:");
            System.out.println(laptop);


            // =========================================
            // 3. Sửa tên sản phẩm
            // =========================================

            String name = laptop.getName();

            String[] words = name.trim().split("\\s+");

            if (words.length >= 2) {

                String temp = words[0];
                words[0] = words[words.length - 1];
                words[words.length - 1] = temp;

                StringBuilder newName = new StringBuilder();

                for (int i = 0; i < words.length; i++) {

                    if (i > 0) {
                        newName.append(" ");
                    }

                    newName.append(words[i]);
                }

                laptop.setName(newName.toString());
            }


            // =========================================
            // 4. Sửa số lượng
            // =========================================

            int quantity = laptop.getQuantity();

            int sign = quantity < 0 ? -1 : 1;

            int reversedQuantity =
                    Integer.parseInt(
                            new StringBuilder(
                                    String.valueOf(Math.abs(quantity))
                            ).reverse().toString()
                    ) * sign;

            laptop.setQuantity(reversedQuantity);


            // =========================================
            // Kiểm tra kết quả
            // =========================================

            System.out.println("Laptop sau khi sửa:");
            System.out.println(laptop);


            // =========================================
            // 5. Gửi Laptop đã sửa về server
            // =========================================

            out.writeObject(laptop);
            out.flush();


            // =========================================
            // 6. Đóng kết nối
            // =========================================

            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}