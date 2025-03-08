import java.io.Serializable;

public class Employee implements Serializable {
    private int employeeId;
    private String pps;
    private String surname;
    private String firstName;
    private char gender;
    private String department;
    private double salary;
    private boolean fullTime;

    public Employee() {
        this(0, "", "", "", '\0', "", 0.0, false);
    }

    public Employee(int employeeId, String pps, String surname, String firstName, char gender, String department, double salary, boolean fullTime) {
        this.employeeId = employeeId;
        this.pps = pps;
        this.surname = surname;
        this.firstName = firstName;
        this.gender = gender;
        this.department = department;
        this.salary = salary;
        this.fullTime = fullTime;
    }

    // Getters and Setters
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getPps() { return pps; }
    public void setPps(String pps) { this.pps = pps; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public char getGender() { return gender; }
    public void setGender(char gender) { this.gender = gender; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public boolean isFullTime() { return fullTime; }
    public void setFullTime(boolean fullTime) { this.fullTime = fullTime; }

    @Override
    public String toString() {
        return "Employee ID: " + employeeId + "\nPPS Number: " + pps + "\nSurname: " + surname +
                "\nFirst Name: " + firstName + "\nGender: " + gender + "\nDepartment: " + department +
                "\nSalary: " + salary + "\nFull Time: " + (fullTime ? "Yes" : "No");
    }
}