import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import net.miginfocom.swing.MigLayout;

public class EmployeeDetails extends JFrame implements ActionListener {
    private RandomFile application = new RandomFile();
    private File file;
    private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    private Employee currentEmployee;
    private long currentByteStart = 0;
    private JTextField searchBySurnameField;
    private JTextField searchByIdField;
    private boolean changesMade = false;

    public EmployeeDetails() {
        setTitle("Employee Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        createMenuBar();
        createContentPane();
        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu navigateMenu = new JMenu("Navigate");
        JMenuItem firstItem = new JMenuItem("First");
        JMenuItem previousItem = new JMenuItem("Previous");
        JMenuItem nextItem = new JMenuItem("Next");
        JMenuItem lastItem = new JMenuItem("Last");
        JMenuItem searchByIdItem = new JMenuItem("Search by ID");
        JMenuItem searchBySurnameItem = new JMenuItem("Search by Surname");
        JMenuItem listAllItem = new JMenuItem("List All");

        firstItem.addActionListener(this);
        previousItem.addActionListener(this);
        nextItem.addActionListener(this);
        lastItem.addActionListener(this);
        searchByIdItem.addActionListener(this);
        searchBySurnameItem.addActionListener(this);
        listAllItem.addActionListener(this);

        navigateMenu.add(firstItem);
        navigateMenu.add(previousItem);
        navigateMenu.add(nextItem);
        navigateMenu.add(lastItem);
        navigateMenu.addSeparator();
        navigateMenu.add(searchByIdItem);
        navigateMenu.add(searchBySurnameItem);
        navigateMenu.add(listAllItem);

        JMenu recordsMenu = new JMenu("Records");
        JMenuItem createItem = new JMenuItem("Create");
        JMenuItem modifyItem = new JMenuItem("Modify");
        JMenuItem deleteItem = new JMenuItem("Delete");

        createItem.addActionListener(this);
        modifyItem.addActionListener(this);
        deleteItem.addActionListener(this);

        recordsMenu.add(createItem);
        recordsMenu.add(modifyItem);
        recordsMenu.add(deleteItem);

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(navigateMenu);
        menuBar.add(recordsMenu);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
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

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "First":
                firstRecord();
                break;
            case "Previous":
                previousRecord();
                break;
            case "Next":
                nextRecord();
                break;
            case "Last":
                lastRecord();
                break;
            case "Search by ID":
                searchById();
                break;
            case "Search by Surname":
                String surname = JOptionPane.showInputDialog(this, "Enter Surname:");
                if (surname != null && !surname.isEmpty()) {
                    searchEmployeeBySurname(surname);
                }
                break;
            case "List All":
                displayAllRecords();
                break;
            case "Create":
                addRecord();
                break;
            case "Modify":
                editRecord();
                break;
            case "Delete":
                deleteRecord();
                break;
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Save As":
                saveFileAs();
                break;
            case "Exit":
                exitApp();
                break;
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("DAT files (*.dat)", "dat"));
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            application.openReadFile(file.getAbsolutePath());
            firstRecord();
            application.closeReadFile();
        }
    }

    private void saveFile() {
        if (file == null) {
            saveFileAs();
        } else {
            application.openWriteFile(file.getAbsolutePath());
            application.closeWriteFile();
            changesMade = false;
            JOptionPane.showMessageDialog(this, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("DAT files (*.dat)", "dat"));
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".dat")) {
                file = new File(file.getAbsolutePath() + ".dat");
            }
            application.createFile(file.getAbsolutePath());
            saveFile();
        }
    }

    private void exitApp() {
        if (changesMade) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to save changes before exiting?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
                System.exit(0);
            } else if (option == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
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

    public static void main(String[] args) {
        new EmployeeDetails();
    }
}