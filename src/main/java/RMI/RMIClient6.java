
package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.Serializable;

public class RMIClient6 {

    public static void main(String[] args) {
        String studentCode = "B22DCCN200"; // Thay bang ma sinh vien
        String qCode = "hmkyTJ4W";

        try {
            // Ket noi den Registry Server
            Registry registry =
                    LocateRegistry.getRegistry("36.50.135.242");

            // Lay ObjectService
            ObjectService service =
                    (ObjectService) registry.lookup("RMIObjectService");

            // Nhan doi tuong tu server
            Serializable received =
                    service.requestObject(studentCode, qCode);

            // Ep kieu doi tuong
            TicketSla ticket = (TicketSla) received;

            String priority = ticket.getPriority();
            int hours = ticket.getOpenedHoursAgo();

            // Xac dinh nguong SLA theo priority
            boolean breached;

            switch (priority) {
                case "CRITICAL":
                    breached = hours > 2;
                    break;
                case "HIGH":
                    breached = hours > 8;
                    break;
                case "MEDIUM":
                    breached = hours > 24;
                    break;
                case "LOW":
                    breached = hours > 72;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Priority khong hop le: " + priority);
            }

            // Cap nhat breached va action
            ticket.setBreached(breached);

            if (!breached) {
                ticket.setAction("MONITOR");
            } else if (priority.equals("CRITICAL")
                    || hours > 96) {
                ticket.setAction("ESCALATE_L2");
            } else {
                ticket.setAction("ESCALATE_L1");
            }

            // Gui doi tuong da cap nhat ve server
            service.submitObject(studentCode, qCode, ticket);

            System.out.println("Ticket ID: " + ticket.getTicketId());
            System.out.println("Priority: " + ticket.getPriority());
            System.out.println("Opened hours ago: "
                    + ticket.getOpenedHoursAgo());
            System.out.println("Breached: " + ticket.isBreached());
            System.out.println("Action: " + ticket.getAction());
            System.out.println("Da gui doi tuong thanh cong.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
