package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Locale;

public class RMIClient4 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 1099; // cổng RMI Registry mặc định
        String studentCode = "B22DCCN200"; // TODO: đổi thành mã sinh viên của bạn
        String qCode = "IgVoTTPA";

        try {
            // a. Kết nối Registry và lấy stub
            Registry registry = LocateRegistry.getRegistry(host, port);
            DataService service = (DataService) registry.lookup("RMIDataService");

            // b. Nhận chuỗi CSV các số thực
            String csv = (String) service.requestData(studentCode, qCode);
            System.out.println("Du lieu nhan duoc: " + csv);

            String[] parts = csv.trim().split("\\s*,\\s*");
            int n = parts.length;
            double[] a = new double[n];
            for (int i = 0; i < n; i++) {
                a[i] = Double.parseDouble(parts[i]);
            }

            // c. Tính trung bình, độ lệch chuẩn (tổng thể), phân vị 95
            double sum = 0;
            for (double v : a) sum += v;
            double avg = sum / n;

            double sq = 0;
            for (double v : a) sq += (v - avg) * (v - avg);
            double stddev = Math.sqrt(sq / n);

            double[] sorted = a.clone();
            Arrays.sort(sorted);
            int idx = (int) Math.ceil(n * 0.95) - 1;
            double p95 = sorted[idx];

            // d. Tạo chuỗi kết quả và gửi lên server
            String result = String.format(Locale.US,
                    "average=%.2f;stddev=%.2f;p95=%.2f", avg, stddev, p95);
            System.out.println("Ket qua: " + result);

            service.submitData(studentCode, qCode, result);
            System.out.println("Da gui ket qua.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        // e. Kết thúc chương trình
    }
}