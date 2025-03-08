import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchBySurnameDialog extends JDialog implements ActionListener {
    private JTextField searchField;
    private JButton search, cancel;
    private EmployeeDetails parent;

    public SearchBySurnameDialog(EmployeeDetails parent) {
        super(parent, "Search by Surname", true);
        this.parent = parent;
        setSize(300, 150);
        setLocationRelativeTo(parent);
        createContentPane();
        setVisible(true);
    }

    private void createContentPane() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Enter Surname:"));
        panel.add(searchField = new JTextField(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(search = new JButton("Search"));
        buttonPanel.add(cancel = new JButton("Cancel"));

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(this);
        cancel.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            String surname = searchField.getText().trim();
            if (!surname.isEmpty()) {
                parent.searchEmployeeBySurname(surname);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a surname to search!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancel) {
            dispose();
        }
    }
}