package workers;

public class Worker extends Employee{
    public Worker(String fullName, String birthdayDate, String hireDate) {
        super(fullName, birthdayDate, hireDate);
    }

    @Override
    public String toString() {
        return "Worker{" +
                super.toString() +
                "} ";
    }

    @Override
    public String getType() {
        return "Worker";
    }
}
