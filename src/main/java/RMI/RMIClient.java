package RMI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class RMIClient {

    public static void main(String[] args) {
        try {
            // Ket noi den RMI Registry
            Registry registry =
                    LocateRegistry.getRegistry("36.50.135.242");

            // Lay doi tuong RMI tu server
            ByteService service =
                    (ByteService) registry.lookup("RMIByteService");

            // a. Nhan mang byte tu server
            byte[] data = service.requestData(
                    "B22DCCN200",
                    "F38nx5bU"
            );

            // Kiem tra mang rong
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

            // Tim phan tu co tan suat xuat hien it nhat
            // Neu bang nhau, chon phan tu xuat hien truoc
            byte minValue = data[0];
            int minCount = frequency.get(minValue);

            for (byte value : data) {
                int count = frequency.get(value);

                if (count < minCount) {
                    minValue = value;
                    minCount = count;
                }
            }

            // c. Tao mang ket qua [phan tu, so lan xuat hien]
            byte[] result = {
                    minValue,
                    (byte) minCount
            };

            // Gui ket qua ve server
            service.submitData(
                    "B22DCCN200",
                    "F38nx5bU",
                    result
            );

            // In ket qua kiem tra
            System.out.println("Mang nhan duoc:");
            for (byte value : data) {
                System.out.print(value + " ");
            }

            System.out.println();
            System.out.println("Phan tu xuat hien it nhat: " + minValue);
            System.out.println("So lan xuat hien: " + minCount);
            System.out.println("Mang ket qua da gui:");

            for (byte value : result) {
                System.out.print(value + " ");
            }

            System.out.println();
            System.out.println("Da gui du lieu thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
