import workers.Manager;
import workers.Worker;

public class Main {
    public static void main(String[] args) {
        Company company = new Company();
        company.addEmployee(new Worker("Vasya", "02.01.2002","17.02.2025"));
        company.addEmployee(new Worker("Vasya2", "02.01.2002","17.02.2025"));
        company.addEmployee(new Worker("Gena", "02.01.2002","17.02.2025"));
        company.addEmployee(new Manager("Venya", "02.01.2002","17.02.2025"));
        company.addEmployee(new Manager("Galya", "02.01.2002","17.02.2025"));
        company.addEmployee(new Manager("Artem", "02.01.2002","17.02.2025"));

        company.assignToManager("Vasya", "Venya");
        company.assignToManager("Vasya2", "Venya");
        company.assignToManager("Gena", "Galya");
        company.assignToManager("Galya", "Artem");
        company.changeType("Gena", "other", "big boss");
        company.showEmployees();
        try {
            company.assignToManager("Vasya", "Galya");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        company.changeType("Galya", "other", "big boss");
        company.showEmployees();

        company.sortEmployeesByName();

        try {
            company.saveToXml("fileX.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("---------------------------------------------");
        company.showEmployees();
        Company loadCompany = new Company();
        System.out.println("---------------------------------------------");
        try {
            loadCompany.loadFromXml("fileX.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        loadCompany.showEmployees();
    }
}
