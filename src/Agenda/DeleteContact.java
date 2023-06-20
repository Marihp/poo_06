package Agenda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteContact {
    private String filePath;

    public DeleteContact(String filePath) {
        this.filePath = filePath;
    }

    public boolean deleteContact(String name) {
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String contactName = data[0];
                    long contactNumber = Long.parseLong(data[1]);
                    String contactEmail = data[2];
                    Contact contact = new Contact(contactName, contactNumber, contactEmail);
                    contacts.add(contact);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la agenda: " + e.getMessage());
        }

        boolean contactExists = false;

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            if (contact.getName().equalsIgnoreCase(name)) {
                contacts.remove(i);
                contactExists = true;
                break;
            }
        }

        if (contactExists) {
            try (FileWriter writer = new FileWriter(filePath)) {
                for (Contact contact : contacts) {
                    writer.write(contact.getName() + "," + contact.getNumber() + "," + contact.getEmail() + System.lineSeparator());
                }
                System.out.println("Contacto eliminado exitosamente");
            } catch (IOException e) {
                System.out.println("Error al eliminar el contacto: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró el contacto: " + name);
        }
        return contactExists;
    }
}
