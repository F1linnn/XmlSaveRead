package workers;

import org.w3c.dom.*;

public class OtherWorker extends Employee{
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty or null!");
        }
        this.description = description;
    }

    public OtherWorker(String fullName, String birthDate, String hireDate, String description) {
        super(fullName, birthDate, hireDate);
        this.description = description;
    }

    @Override
    public Element toXmlElement(Document doc) {
        Element el = super.toXmlElement(doc);
        Element description = doc.createElement("Description");
        description.appendChild(doc.createTextNode(this.description));
        el.appendChild(description);
        return el;

    }

    @Override
    public String toString() {
        return "OtherWorker{" + super.toString() +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public String getType() {
        return "Other";
    }
}
