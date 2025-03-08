import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class RandomFile {
    private RandomAccessFile output;
    private RandomAccessFile input;

    public void openReadFile(String fileName) {
        try {
            input = new RandomAccessFile(fileName, "r");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error opening file for reading!");
        }
    }

    public void openWriteFile(String fileName) {
        try {
            output = new RandomAccessFile(fileName, "rw");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error opening file for writing!");
        }
    }

    public void closeReadFile() {
        try {
            if (input != null) input.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error closing file!");
        }
    }

    public void closeWriteFile() {
        try {
            if (output != null) output.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error closing file!");
        }
    }

    public Employee readRecords(long byteStart) {
        try {
            input.seek(byteStart);
            RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();
            record.read(input);
            return record;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading record!");
            return null;
        }
    }

    public long getFirst() {
        return 0;
    }

    public long getNext(long byteStart) {
        return byteStart + RandomAccessEmployeeRecord.SIZE;
    }

    public long getPrevious(long byteStart) {
        return byteStart - RandomAccessEmployeeRecord.SIZE;
    }

    public long getLast() {
        try {
            return input.length() - RandomAccessEmployeeRecord.SIZE;
        } catch (IOException e) {
            return 0;
        }
    }

    public Employee findRecord(int id) {
        try {
            long byteStart = getFirst();
            Employee emp = readRecords(byteStart);
            while (emp != null) {
                if (emp.getEmployeeId() == id) {
                    return emp;
                }
                byteStart = getNext(byteStart);
                emp = readRecords(byteStart);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error finding record!");
        }
        return null;
    }

    public long addRecords(Employee employee) {
        try {
            RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord(
                employee.getEmployeeId(), employee.getPps(), employee.getSurname(),
                employee.getFirstName(), employee.getGender(), employee.getDepartment(),
                employee.getSalary(), employee.isFullTime()
            );
            output.seek(output.length());
            record.write(output);
            return output.length() - RandomAccessEmployeeRecord.SIZE;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error adding record!");
            return -1;
        }
    }

    public void deleteRecord(long byteStart) {
        try {
            RandomAccessEmployeeRecord record = new RandomAccessEmployeeRecord();
            output.seek(byteStart);
            record.write(output);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error deleting record!");
        }
    }
    
    public void createFile(String fileName) {
        try {
            output = new RandomAccessFile(fileName, "rw");
            output.setLength(0);
            output.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error creating file!");
        }
    }
}