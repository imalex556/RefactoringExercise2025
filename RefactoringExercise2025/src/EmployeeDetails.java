import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EmployeeDetails extends JFrame implements ActionListener {
    private RandomFile application = new RandomFile();
    private File file;
    private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname;
    private Employee currentEmployee;
    private long currentByteStart = 0;
	private JTextField searchBySurnameField;
	JTextField searchByIdField;

    public EmployeeDetails() {
        setTitle("Employee Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createContentPane();
        setVisible(true);
    }

    private void createContentPane() {
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField = new JTextField(20));
        panel.add(new JLabel("PPS Number:"));
        panel.add(ppsField = new JTextField(20));
        panel.add(new JLabel("Surname:"));
        panel.add(surnameField = new JTextField(20));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField = new JTextField(20));
        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo = new JComboBox<>(new String[]{"", "M", "F"}));
        panel.add(new JLabel("Department:"));
        panel.add(departmentCombo = new JComboBox<>(new String[]{"", "Administration", "Production", "Transport", "Management"}));
        panel.add(new JLabel("Salary:"));
        panel.add(salaryField = new JTextField(20));
        panel.add(new JLabel("Full Time:"));
        panel.add(fullTimeCombo = new JComboBox<>(new String[]{"", "Yes", "No"}));

        JPanel buttonPanel = new JPanel();
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

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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
            // Prompt user for surname and call method
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

    // Add Record
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

    // Edit Record
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

    // Delete Record
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

    // Display All Records
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

    // Search by ID
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

    // Search by Surname
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
    
    public void searchEmployeeById(int id) {
        application.openReadFile(file.getAbsolutePath());
        Employee found = application.findRecord(id);
        application.closeReadFile();
        
        if (found != null) {
            displayRecords(found);
        } else {
            JOptionPane.showMessageDialog(this, "Record not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Navigation Methods
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

    // Helper Methods
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