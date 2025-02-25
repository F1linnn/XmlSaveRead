package workers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
public class Manager extends Employee{
    private List<Employee> subordinates;
    public Manager(String fullName, String birthdayDate, String hireDate) {
        super(fullName, birthdayDate, hireDate);
        this.subordinates = new ArrayList<>();
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }



    public void addSubordinate(Employee e) {
        if(e != null) // && !(e instanceof Manager)
            this.subordinates.add(e);
    }

    public boolean hasSubordinate(Employee e) {
        return subordinates.contains(e);
    }

    public void removeSubordinates(Employee e) {
        if(e != null)
            this.subordinates.remove(e);
    }

    @Override
    public Element toXmlElement(Document doc) {
        Element manager = super.toXmlElement(doc);
        Element subordinatesEl = doc.createElement("Subordinates");
        for (Employee e : subordinates) {
            subordinatesEl.appendChild(e.toXmlElement(doc));
        }
        manager.appendChild(subordinatesEl);
        return manager;
    }

    @Override
    public String getType() {
        return "Manager";
    }

    @Override
    public String toString() {
        return "Manager{" + super.toString()  + ", subordinates=" + subordinates +
                "}";
    }
}
