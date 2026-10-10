package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class RMIClient1 {

    public static void main(String[] args) {
        try {
            // Ket noi den RMI Server
            Registry registry =
                    LocateRegistry.getRegistry("36.50.135.242");

            // Lay doi tuong ByteService
            ByteService service =
                    (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte tu server
            byte[] data = service.requestData(
                    "B22DCCN200",
                    "AnBBxdqq"
            );

            if (data == null || data.length == 0) {
                System.out.println("Mang du lieu rong!");
                return;
            }

            // b. Dem so lan xuat hien cua tung phan tu
            Map<Byte, Integer> frequency = new HashMap<>();

            for (byte value : data) {
                frequency.put(
                        value,
                        frequency.getOrDefault(value, 0) + 1
                );
            }

            // Tim phan tu xuat hien nhieu nhat
            // Neu bang tan suat, chon phan tu xuat hien truoc
            byte maxValue = data[0];
            int maxCount = frequency.get(maxValue);

            for (byte value : data) {
                int count = frequency.get(value);

                if (count > maxCount) {
                    maxValue = value;
                    maxCount = count;
                }
            }

            // c. Tao mang ket qua [phan tu, tan suat]
            byte[] result = {
                    maxValue,
                    (byte) maxCount
            };

            // Gui ket qua ve server
            service.submitData(
                    "B22DCCN200",
                    "AnBBxdqq",
                    result
            );

            System.out.println("Phan tu xuat hien nhieu nhat: "
                    + maxValue);
            System.out.println("Tan suat: " + maxCount);
            System.out.println("Mang ket qua:");

            for (byte value : result) {
                System.out.print(value + " ");
            }

            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

