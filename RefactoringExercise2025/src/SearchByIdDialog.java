import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchByIdDialog extends JDialog implements ActionListener {
    private JTextField searchField;
    private JButton search, cancel;
    private EmployeeDetails parent;

    public SearchByIdDialog(EmployeeDetails parent) {
        super(parent, "Search by ID", true);
        this.parent = parent;
        setSize(300, 150);
        setLocationRelativeTo(parent);
        createContentPane();
        setVisible(true);
    }

    private void createContentPane() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Enter ID:"));
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
            searchById();
        } else if (e.getSource() == cancel) {
            dispose();
        }
    }

    private void searchById() {
        try {
            int id = Integer.parseInt(searchField.getText());
            parent.searchEmployeeById(id);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}