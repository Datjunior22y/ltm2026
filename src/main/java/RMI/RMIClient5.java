package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RMIClient5{
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 1099; // cổng RMI Registry mặc định
        String studentCode = "B22DCCN200"; // TODO: đổi thành mã sinh viên của bạn
        String qCode = "vkwZcrKK";

        try {
            // a. Kết nối Registry và lấy stub
            Registry registry = LocateRegistry.getRegistry(host, port);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // b. Nhận mảng số nguyên từ server
            int[] arr = (int[]) service.requestData(studentCode, qCode);
            System.out.println("Du lieu nhan duoc: " + Arrays.toString(arr));

            // c. Tìm các đỉnh cục bộ (chỉ số bắt đầu từ 1)
            List<Integer> peaks = new ArrayList<>();
            int n = arr.length;
            for (int i = 0; i < n; i++) {
                boolean leftOk = (i == 0) || arr[i] > arr[i - 1];
                boolean rightOk = (i == n - 1) || arr[i] > arr[i + 1];
                if (leftOk && rightOk) {
                    peaks.add(i + 1);
                }
            }
            System.out.println("Vi tri dinh cuc bo: " + peaks);

            // d. Gửi kết quả lên server
            service.submitData(studentCode, qCode, peaks);
            System.out.println("Da gui ket qua.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        // e. Kết thúc chương trình
    }
}