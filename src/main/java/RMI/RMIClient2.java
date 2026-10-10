
package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.URI;

public class RMIClient2 {

    public static void main(String[] args) {
        try {
            // Ket noi den RMI Registry
            Registry registry =
                    LocateRegistry.getRegistry("36.50.135.242");

            // Lay doi tuong CharacterService
            CharacterService service =
                    (CharacterService) registry.lookup(
                            "RMICharacterService"
                    );

            // a. Nhan chuoi tu server
            String data = service.requestCharacter(
                    "B22DCCN200",
                    "Pfh71BAF"
            );

            // b. Ma hoa URL theo quy tac duong dan URI
            String encoded = new URI(
                    null, null, data, null
            ).toASCIIString();

            // c. Gui chuoi da ma hoa ve server
            service.submitCharacter(
                    "B22DCCN200",
                    "Pfh71BAF",
                    encoded
            );

            System.out.println("Chuoi ban dau: " + data);
            System.out.println("Chuoi da ma hoa: " + encoded);
            System.out.println("Da gui ket qua len server.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
