package workers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.*;

public abstract class Employee {
    private String fullName;
    private Date birthDate;
    private Date hireDate;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");


    public Employee(String fullName, String birthDate, String hireDate) {
        setFullName(fullName);
        this.birthDate = parseDate(birthDate);
        this.hireDate = parseDate(hireDate);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Fullname must not be empty or null!");
        }
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthdayDate(String birthDate) {
        this.birthDate = parseDate(birthDate);
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = parseDate(hireDate);
    }

    private Date parseDate(String date) {
        try {
            DATE_FORMAT.setLenient(false);
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date: " + date);
        }
    }
    public Element toXmlElement(Document doc) {
        Element employee = doc.createElement(getType());
        employee.appendChild(createElement(doc, "FullName", fullName));
        employee.appendChild(createElement(doc, "BirthDate", DATE_FORMAT.format(birthDate)));
        employee.appendChild(createElement(doc, "HireDate", DATE_FORMAT.format(hireDate)));
        return employee;
    }

    protected Element createElement(Document doc, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }

    @Override
    public String toString() {
        return "Info{" +
                "fullName='" + fullName + '\'' +
                ", birthDate=" + DATE_FORMAT.format(birthDate) +
                ", hireDate=" +  DATE_FORMAT.format(hireDate) +
                '}';
    }

    public abstract String getType();
}