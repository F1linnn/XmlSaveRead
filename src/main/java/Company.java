import workers.*;

import java.util.*;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.function.Function;

import static workers.Employee.DATE_FORMAT;

public class Company {
    private List<Employee> employees;

    public Company() {
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee e) {
        employees.add(e);
    }

    public void showEmployees(){
        for (int i = 0; i < employees.size(); i++) {
            System.out.println(employees.get(i));
        }
    }

    public void removeEmployee(String fullName) {
        employees.removeIf(e -> e.getFullName().equalsIgnoreCase(fullName));
    }

    public void assignToManager(String employeeName, String managerName) {
        Employee emp = findEmployeeByName(employeeName);
        Employee potentialManager = findEmployeeByName(managerName);

        if (emp == null) {
            throw new IllegalArgumentException("Worker is not found: " + employeeName);
        }
        if (potentialManager == null || !(potentialManager instanceof Manager)) {
            throw new IllegalArgumentException("Manager is not found or it is not Manager: " + managerName);
        }

        Manager manager = (Manager) potentialManager;


        for (Employee e : employees) {
            if (e instanceof Manager && ((Manager) e).hasSubordinate(emp)) {
                throw new IllegalArgumentException("The employee("+emp.getFullName()+") has a another manager(" + e.getFullName()+ ")");
            }
        }

        manager.addSubordinate(emp);
    }

    public void changeType(String oldEmployee, String newType, String description) {
        Employee oldType = findEmployeeByName(oldEmployee);
        employees.remove(oldType);
        Employee newEmployee;
        switch (newType.toLowerCase()) {
            case "worker":
                newEmployee = new Worker(oldType.getFullName(), DATE_FORMAT.format(oldType.getBirthDate()), DATE_FORMAT.format(oldType.getHireDate()));
                break;
            case "manager":
                newEmployee = new Manager(oldType.getFullName(), DATE_FORMAT.format(oldType.getBirthDate()), DATE_FORMAT.format(oldType.getHireDate()));
                break;
            case "other":
                newEmployee = new OtherWorker(oldType.getFullName(), DATE_FORMAT.format(oldType.getBirthDate()), DATE_FORMAT.format(oldType.getHireDate()), description);
                for (Employee e : employees) {
                    if (e instanceof Manager && ((Manager) e).hasSubordinate(oldType)) {
                        ((Manager) e).removeSubordinates(oldType);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип сотрудника");
        }
        employees.add(newEmployee);
    }

    public void sortEmployeesByName() {
        employees.sort(Comparator.comparing(Employee::getFullName));
    }

    public void sortEmployeesByHireDate() {
        employees.sort(Comparator.comparing(Employee::getHireDate));
    }



    private Employee findEmployeeByName(String name) {
        return employees.stream()
                .filter(e -> e.getFullName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void saveToXml(String filename) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("Company");
        doc.appendChild(root);

        for (Employee e : employees) {
            root.appendChild(e.toXmlElement(doc));
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source, result);
    }

    public void loadFromXml(String filename) throws Exception {
        employees.clear();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(filename));
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        Map<String, Manager> managersMap = new HashMap<>();

        Map<String, Function<Element, Employee>> employeeFactory = Map.of(
                "Worker", e -> new Worker(
                        e.getElementsByTagName("FullName").item(0).getTextContent(),
                        e.getElementsByTagName("BirthDate").item(0).getTextContent(),
                        e.getElementsByTagName("HireDate").item(0).getTextContent()
                ),
                "Manager", e -> {
                    Manager manager = new Manager(
                            e.getElementsByTagName("FullName").item(0).getTextContent(),
                            e.getElementsByTagName("BirthDate").item(0).getTextContent(),
                            e.getElementsByTagName("HireDate").item(0).getTextContent()
                    );
                    managersMap.put(manager.getFullName(), manager);
                    return manager;
                },
                "Other", e -> new OtherWorker(
                        e.getElementsByTagName("FullName").item(0).getTextContent(),
                        e.getElementsByTagName("BirthDate").item(0).getTextContent(),
                        e.getElementsByTagName("HireDate").item(0).getTextContent(),
                        e.getElementsByTagName("Description").item(0).getTextContent()
                )
        );

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String tag = element.getTagName();
                Employee employee = employeeFactory.getOrDefault(tag, e -> null).apply(element);
                if (employee != null) {
                    employees.add(employee);
                }
            }
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("Manager")) {
                Element element = (Element) node;
                String managerName = element.getElementsByTagName("FullName").item(0).getTextContent();
                Manager manager = managersMap.get(managerName);
                if (manager != null) {
                    NodeList subordinatesList = element.getElementsByTagName("Subordinates").item(0).getChildNodes();
                    for (int j = 0; j < subordinatesList.getLength(); j++) {
                        Node subNode = subordinatesList.item(j);
                        if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                            String subName = ((Element) subNode).getElementsByTagName("FullName").item(0).getTextContent();
                            for (Employee e : employees) {
                                if (e.getFullName().equals(subName)) {
                                    manager.addSubordinate(e);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

