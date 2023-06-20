package Agenda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateContact {
    private String filePath;

    public UpdateContact(String filePath) {
        this.filePath = filePath;
    }

    public boolean updateContact(String name, long newNumber, String newEmail) {
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

        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                contact.setNumber(newNumber);
                contact.setEmail(newEmail);
                contactExists = true;
                break;
            }
        }

        if (contactExists) {
            try (FileWriter writer = new FileWriter(filePath)) {
                for (Contact contact : contacts) {
                    writer.write(contact.getName() + "," + contact.getNumber() + "," + contact.getEmail() + System.lineSeparator());
                }
                System.out.println("Contacto actualizado exitosamente");
            } catch (IOException e) {
                System.out.println("Error al actualizar el contacto: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró el contacto: " + name);
        }
        return contactExists;
    }
}
