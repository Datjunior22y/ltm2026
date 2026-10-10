package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient3 {

    // Chuyen so nguyen sang so La Ma
    public static String toRoman(int number) {
        int[] values = {
                1000, 900, 500, 400, 100, 90,
                50, 40, 10, 9, 5, 4, 1
        };

        String[] symbols = {
                "M", "CM", "D", "CD", "C", "XC",
                "L", "XL", "X", "IX", "V", "IV", "I"
        };

        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                roman.append(symbols[i]);
                number -= values[i];
            }
        }

        return roman.toString();
    }

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

            // a. Nhan chuoi so thap phan tu server
            String data = service.requestCharacter(
                    "B22DCCN200",
                    "iSMUcf7E"
            );

            // Chuyen chuoi nhan duoc thanh so nguyen
            int number = Integer.parseInt(data.trim());

            // b. Chuyen so thap phan sang so La Ma
            String result = toRoman(number);

            // c. Gui ket qua ve server
            service.submitCharacter(
                    "B22DCCN200",
                    "iSMUcf7E",
                    result
            );

            // In ket qua
            System.out.println("So nhan duoc: " + data);
            System.out.println("So La Ma: " + result);
            System.out.println("Da gui ket qua len server!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}