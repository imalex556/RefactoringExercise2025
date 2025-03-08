import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

public class EmployeeSummaryDialog extends JDialog implements ActionListener {
    private Vector<Object> allEmployees;
    private JButton back;

    public EmployeeSummaryDialog(Vector<Object> allEmployees) {
        setTitle("Employee Summary");
        setModal(true);
        this.allEmployees = allEmployees;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(summaryPane());
        setContentPane(scrollPane);

        setSize(850, 500);
        setLocation(350, 250);
        setVisible(true);
    }

    public Container summaryPane() {
        JPanel summaryDialog = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTable employeeTable;
        DefaultTableModel tableModel;
        Vector<String> header = new Vector<>();
        String[] headerName = {"ID", "PPS Number", "Surname", "First Name", "Gender", "Department", "Salary", "Full Time"};
        int[] colWidth = {15, 100, 120, 120, 50, 120, 80, 80};

        for (String name : headerName) {
            header.add(name);
        }

        tableModel = new DefaultTableModel() {
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return Integer.class;
                    case 4: return Character.class;
                    case 6: return Double.class;
                    case 7: return Boolean.class;
                    default: return String.class;
                }
            }
        };

        employeeTable = new JTable(tableModel);
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumn(headerName[i]).setMinWidth(colWidth[i]);
        }

        employeeTable.setEnabled(false);
        employeeTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        buttonPanel.add(back = new JButton("Back"));
        back.addActionListener(this);

        summaryDialog.add(buttonPanel, BorderLayout.SOUTH);
        summaryDialog.add(scrollPane, BorderLayout.CENTER);

        return summaryDialog;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            dispose();
        }
    }

    static class DecimalFormatRenderer extends DefaultTableCellRenderer {
        private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            value = format.format((Number) value);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}