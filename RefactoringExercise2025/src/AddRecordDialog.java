import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddRecordDialog extends JDialog implements ActionListener {
    private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    private JButton save, cancel;
    private EmployeeDetails parent;

    public AddRecordDialog(EmployeeDetails parent) {
        super(parent, "Add Record", true);
        this.parent = parent;
        setSize(400, 300);
        setLocationRelativeTo(parent);
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
        buttonPanel.add(save = new JButton("Save"));
        buttonPanel.add(cancel = new JButton("Cancel"));

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        save.addActionListener(this);
        cancel.addActionListener(this);
    }

    public void setFields(Employee employee) {
        idField.setText(String.valueOf(employee.getEmployeeId()));
        ppsField.setText(employee.getPps());
        surnameField.setText(employee.getSurname());
        firstNameField.setText(employee.getFirstName());
        genderCombo.setSelectedItem(String.valueOf(employee.getGender()));
        departmentCombo.setSelectedItem(employee.getDepartment());
        salaryField.setText(String.valueOf(employee.getSalary()));
        fullTimeCombo.setSelectedItem(employee.isFullTime() ? "Yes" : "No");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            saveRecord();
        } else if (e.getSource() == cancel) {
            dispose();
        }
    }

    private void saveRecord() {
        try {
            int id = Integer.parseInt(idField.getText());
            String pps = ppsField.getText().trim();
            String surname = surnameField.getText().trim();
            String firstName = firstNameField.getText().trim();
            char gender = genderCombo.getSelectedItem().toString().charAt(0);
            String department = departmentCombo.getSelectedItem().toString();
            double salary = Double.parseDouble(salaryField.getText());
            boolean fullTime = fullTimeCombo.getSelectedIndex() == 1;

            if (validateInput(pps, surname, firstName, salary)) {
                Employee newEmployee = new Employee(id, pps, surname, firstName, gender, department, salary, fullTime);
                parent.addRecord(newEmployee);
                JOptionPane.showMessageDialog(this, "Record saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } 

    private boolean validateInput(String pps, String surname, String firstName, double salary) {
        if (pps.isEmpty() || surname.isEmpty() || firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!pps.matches("\\d{7}[A-Z]{1,2}")) {
            JOptionPane.showMessageDialog(this, "Invalid PPS format! (7 digits + 1-2 letters)", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (salary <= 0) {
            JOptionPane.showMessageDialog(this, "Salary must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}