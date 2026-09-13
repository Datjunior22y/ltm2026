package org.example;
import TCP.Customer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class tcp_object_2 {

    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "B22DCCN200";
        String qCode = "eZF99uLE";

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
            // 2. Nhận đối tượng Customer
            // =========================================

            Customer customer = (Customer) in.readObject();

            System.out.println("Customer nhận được:");
            System.out.println(customer);


            // =========================================
            // 3a. Chuẩn hóa tên
            //
            // Ví dụ:
            // nguyen van hai duong
            // -> DUONG, Nguyen Van Hai
            // =========================================

            String name = customer.getName().trim();

            String[] words = name.split("\\s+");

            if (words.length > 0) {

                String lastName = words[words.length - 1];

                StringBuilder fullName = new StringBuilder();

                // Họ + tên đệm + tên
                for (int i = 0; i < words.length - 1; i++) {

                    if (i > 0) {
                        fullName.append(" ");
                    }

                    String word = words[i];

                    word = word.substring(0, 1).toUpperCase()
                            + word.substring(1).toLowerCase();

                    fullName.append(word);
                }

                // Tên cuối viết hoa toàn bộ
                String newName =
                        lastName.toUpperCase()
                                + ", "
                                + fullName.toString();

                customer.setName(newName);
            }


            // =========================================
            // 3b. Đổi ngày sinh
            //
            // mm-dd-yyyy
            // ->
            // dd/mm/yyyy
            // =========================================

            String dob = customer.getDayOfBirth().trim();

            String[] date = dob.split("-");

            if (date.length == 3) {

                String month = date[0];
                String day = date[1];
                String year = date[2];

                String newDob =
                        day + "/" + month + "/" + year;

                customer.setDayOfBirth(newDob);
            }


            // =========================================
            // 3c. Tạo username
            //
            // nguyen van hai duong
            // -> nvhduong
            // =========================================

            // Lấy tên gốc trước khi đổi format
            String originalName = name;

            String[] nameParts = originalName.split("\\s+");

            StringBuilder username = new StringBuilder();

            // Lấy chữ cái đầu của họ và tên đệm
            for (int i = 0; i < nameParts.length - 1; i++) {

                username.append(
                        Character.toLowerCase(
                                nameParts[i].charAt(0)
                        )
                );
            }

            // Ghép toàn bộ tên cuối
            username.append(
                    nameParts[nameParts.length - 1].toLowerCase()
            );

            customer.setUserName(username.toString());


            // =========================================
            // Kiểm tra kết quả
            // =========================================

            System.out.println("Customer sau khi chuẩn hóa:");
            System.out.println(customer);


            // =========================================
            // 4. Gửi Customer đã sửa về server
            // =========================================

            out.writeObject(customer);
            out.flush();


            // =========================================
            // 5. Đóng kết nối
            // =========================================

            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
