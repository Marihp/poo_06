package Agenda;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AgendaGUI extends JFrame implements ActionListener {
    private JButton btnCreate;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnRead;
    private JTable contactTable;

    private CreateContact createContact;
    private UpdateContact updateContact;
    private DeleteContact deleteContact;
    private ReadContact readContact;

    public AgendaGUI() {
        setTitle("Agenda");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        btnCreate = new JButton("Crear Contacto");
        btnUpdate = new JButton("Actualizar Contacto");
        btnDelete = new JButton("Eliminar Contacto");
        btnRead = new JButton("Leer Agenda");

        btnCreate.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnRead.addActionListener(this);

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRead);

        add(buttonPanel, BorderLayout.NORTH);

        // Tabla de contactos
        contactTable = new JTable();
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        contactTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = contactTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String name = (String) contactTable.getValueAt(selectedRow, 0);
                        long number = (long) contactTable.getValueAt(selectedRow, 1);
                        String email = (String) contactTable.getValueAt(selectedRow, 2);

                        // Aquí puedes realizar alguna acción con el contacto seleccionado
                        System.out.println("Contacto seleccionado: " + name + ", " + number + ", " + email);
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(contactTable);
        add(tableScrollPane, BorderLayout.CENTER);

        createContact = new CreateContact("agenda.txt");
        updateContact = new UpdateContact("agenda.txt");
        deleteContact = new DeleteContact("agenda.txt");
        readContact = new ReadContact("agenda.txt");

        updateContactTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCreate) {
            // Lógica para crear un contacto
            String name = JOptionPane.showInputDialog(this, "Ingrese el nombre del contacto:");
            String numberStr = JOptionPane.showInputDialog(this, "Ingrese el número de teléfono:");
            long number = Long.parseLong(numberStr);
            String email = JOptionPane.showInputDialog(this, "Ingrese el correo electrónico:");
            boolean created = createContact.createContact(name, number, email);
            if (created) {
                JOptionPane.showMessageDialog(this, "Contacto creado exitosamente.");
                updateContactTable();
            } else {
                JOptionPane.showMessageDialog(this, "El contacto ya existe en la agenda.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnUpdate) {
            // Lógica para actualizar un contacto
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow != -1) {
                String name = (String) contactTable.getValueAt(selectedRow, 0);
                String newNumberStr = JOptionPane.showInputDialog(this, "Ingrese el nuevo número de teléfono:");
                long newNumber = Long.parseLong(newNumberStr);
                String newEmail = JOptionPane.showInputDialog(this, "Ingrese el nuevo correo electrónico:");
                updateContact.updateContact(name, newNumber, newEmail);
                updateContactTable();
                JOptionPane.showMessageDialog(this, "Contacto actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un contacto de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnDelete) {
            // Lógica para eliminar un contacto
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow != -1) {
                String name = (String) contactTable.getValueAt(selectedRow, 0);
                deleteContact.deleteContact(name);
                updateContactTable();
                JOptionPane.showMessageDialog(this, "Contacto eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un contacto de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnRead) {
            // Lógica para leer la agenda
            readContact.readContacts();
            updateContactTable();
        }
    }

    private void updateContactTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Número de teléfono");
        tableModel.addColumn("Correo electrónico");

        // Obtener los contactos de la agenda y agregarlos a la tabla
        List<Contact> contacts = readContact.getContacts();
        for (Contact contact : contacts) {
            Object[] rowData = {contact.getName(), contact.getNumber(), contact.getEmail()};
            tableModel.addRow(rowData);
        }

        contactTable.setModel(tableModel); // Actualizar el modelo de la tabla
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AgendaGUI agendaGUI = new AgendaGUI();
                agendaGUI.setVisible(true);
            }
        });
    }
}
