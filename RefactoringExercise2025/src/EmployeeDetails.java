import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import net.miginfocom.swing.MigLayout;

public class EmployeeDetails extends JFrame implements ActionListener {
    private RandomFile application = new RandomFile();
    private File file;
    private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname;
    private Employee currentEmployee;
    private long currentByteStart = 0;
    private JTextField searchBySurnameField;
    private JTextField searchByIdField;

    public EmployeeDetails() {
        setTitle("Employee Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createContentPane();
        setVisible(true);
    }

    private void createContentPane() {
        JPanel mainPanel = new JPanel(new MigLayout("wrap 2", "[][grow]", "[][][][][][][][][grow]"));

        mainPanel.add(new JLabel("ID:"), "align right");
        mainPanel.add(idField = new JTextField(20), "growx");
        mainPanel.add(new JLabel("PPS Number:"), "align right");
        mainPanel.add(ppsField = new JTextField(20), "growx");
        mainPanel.add(new JLabel("Surname:"), "align right");
        mainPanel.add(surnameField = new JTextField(20), "growx");
        mainPanel.add(new JLabel("First Name:"), "align right");
        mainPanel.add(firstNameField = new JTextField(20), "growx");
        mainPanel.add(new JLabel("Gender:"), "align right");
        mainPanel.add(genderCombo = new JComboBox<>(new String[]{"", "M", "F"}), "growx");
        mainPanel.add(new JLabel("Department:"), "align right");
        mainPanel.add(departmentCombo = new JComboBox<>(new String[]{"", "Administration", "Production", "Transport", "Management"}), "growx");
        mainPanel.add(new JLabel("Salary:"), "align right");
        mainPanel.add(salaryField = new JTextField(20), "growx");
        mainPanel.add(new JLabel("Full Time:"), "align right");
        mainPanel.add(fullTimeCombo = new JComboBox<>(new String[]{"", "Yes", "No"}), "growx");

        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[][][][][][][][][]", "[]"));
        buttonPanel.add(first = new JButton("First"));
        buttonPanel.add(previous = new JButton("Previous"));
        buttonPanel.add(next = new JButton("Next"));
        buttonPanel.add(last = new JButton("Last"));
        buttonPanel.add(add = new JButton("Add"));
        buttonPanel.add(edit = new JButton("Edit"));
        buttonPanel.add(deleteButton = new JButton("Delete"));
        buttonPanel.add(displayAll = new JButton("Display All"));
        buttonPanel.add(searchId = new JButton("Search by ID"));
        buttonPanel.add(searchSurname = new JButton("Search by Surname"));

        first.addActionListener(this);
        previous.addActionListener(this);
        next.addActionListener(this);
        last.addActionListener(this);
        add.addActionListener(this);
        edit.addActionListener(this);
        deleteButton.addActionListener(this);
        displayAll.addActionListener(this);
        searchId.addActionListener(this);
        searchSurname.addActionListener(this);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add) {
            addRecord();
        } else if (e.getSource() == edit) {
            editRecord();
        } else if (e.getSource() == deleteButton) {
            deleteRecord();
        } else if (e.getSource() == displayAll) {
            displayAllRecords();
        } else if (e.getSource() == searchId) {
            searchById();
        } else if (e.getSource() == searchSurname) {
            String surname = JOptionPane.showInputDialog(this, "Enter Surname:");
            if (surname != null && !surname.isEmpty()) {
                searchEmployeeBySurname(surname);
            }
        } else if (e.getSource() == first) {
            firstRecord();
        } else if (e.getSource() == previous) {
            previousRecord();
        } else if (e.getSource() == next) {
            nextRecord();
        } else if (e.getSource() == last) {
            lastRecord();
        }
    }

    public void addRecord() {
        AddRecordDialog addDialog = new AddRecordDialog(this);
        addDialog.setVisible(true);
    }

    public void addRecord(Employee employee) {
        application.openWriteFile(file.getAbsolutePath());
        currentByteStart = application.addRecords(employee);
        application.closeWriteFile();
        refreshDisplay();
    }

    private void editRecord() {
        if (currentEmployee != null) {
            AddRecordDialog editDialog = new AddRecordDialog(this);
            editDialog.setFields(currentEmployee);
            editDialog.setVisible(true);
            refreshDisplay();
        } else {
            JOptionPane.showMessageDialog(this, "No record selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        if (currentEmployee != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                application.deleteRecord(currentByteStart);
                JOptionPane.showMessageDialog(this, "Record deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshDisplay();
            }
        }
    }

    private void displayAllRecords() {
        StringBuilder allRecords = new StringBuilder();
        application.openReadFile(file.getAbsolutePath());
        Employee emp = application.readRecords(application.getFirst());
        while (emp != null) {
            allRecords.append(emp.toString()).append("\n\n");
            emp = application.readRecords(application.getNext(currentByteStart));
        }
        application.closeReadFile();
        JOptionPane.showMessageDialog(this, allRecords.toString(), "All Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchById() {
        String searchId = JOptionPane.showInputDialog(this, "Enter Employee ID:");
        if (searchId != null && !searchId.isEmpty()) {
            application.openReadFile(file.getAbsolutePath());
            Employee found = application.findRecord(Integer.parseInt(searchId));
            application.closeReadFile();
            if (found != null) {
                displayRecords(found);
            } else {
                JOptionPane.showMessageDialog(this, "Record not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void searchEmployeeBySurname(String surname) {
        application.openReadFile(file.getAbsolutePath());
        Employee emp = application.readRecords(application.getFirst());
        boolean found = false;

        while (emp != null && !found) {
            if (emp.getSurname().equalsIgnoreCase(surname)) {
                displayRecords(emp);
                found = true;
            }
            emp = application.readRecords(application.getNext(currentByteStart));
        }

        application.closeReadFile();
        if (!found) {
            JOptionPane.showMessageDialog(this, "Record not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void firstRecord() {
        currentByteStart = application.getFirst();
        refreshDisplay();
    }

    private void previousRecord() {
        currentByteStart = application.getPrevious(currentByteStart);
        refreshDisplay();
    }

    private void nextRecord() {
        currentByteStart = application.getNext(currentByteStart);
        refreshDisplay();
    }

    private void lastRecord() {
        currentByteStart = application.getLast();
        refreshDisplay();
    }

    private void refreshDisplay() {
        application.openReadFile(file.getAbsolutePath());
        displayRecords(application.readRecords(currentByteStart));
        application.closeReadFile();
    }

    private void displayRecords(Employee emp) {
        if (emp != null) {
            idField.setText(String.valueOf(emp.getEmployeeId()));
            ppsField.setText(emp.getPps());
            surnameField.setText(emp.getSurname());
            firstNameField.setText(emp.getFirstName());
            genderCombo.setSelectedItem(String.valueOf(emp.getGender()));
            departmentCombo.setSelectedItem(emp.getDepartment());
            salaryField.setText(String.valueOf(emp.getSalary()));
            fullTimeCombo.setSelectedItem(emp.isFullTime() ? "Yes" : "No");
        }
    }

    public static void main(String[] args) {
        new EmployeeDetails();
    }
}