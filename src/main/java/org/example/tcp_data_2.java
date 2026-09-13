package org.example;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
public class tcp_data_2 {
    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "B22DCCN200";
        String qCode = "R2OAlqXp";

        try {
            Socket socket = new Socket(host, port);

            DataInputStream in =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());



            String request = studentCode + ";" + qCode;

            out.writeUTF(request);
            out.flush();




            String encrypted = in.readUTF();
            int s = in.readInt();

            System.out.println("Chuỗi mã hóa: " + encrypted);
            System.out.println("s = " + s);



            StringBuilder decrypted = new StringBuilder();

            // Đưa s về khoảng 0..25
            s = s % 26;

            for (int i = 0; i < encrypted.length(); i++) {

                char c = encrypted.charAt(i);

                // Chữ cái in hoa
                if (c >= 'A' && c <= 'Z') {

                    char decoded =
                            (char) ((c - 'A' - s + 26) % 26 + 'A');

                    decrypted.append(decoded);
                }


                else if (c >= 'a' && c <= 'z') {

                    char decoded =
                            (char) ((c - 'a' - s + 26) % 26 + 'a');

                    decrypted.append(decoded);
                }


                else {
                    decrypted.append(c);
                }
            }

            String result = decrypted.toString();

            System.out.println("Thông điệp giải mã:");
            System.out.println(result);

            out.writeUTF(result);
            out.flush();


            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
