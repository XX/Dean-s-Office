package ru.sgu.csit.inoc.deansoffice.aos;

import ru.sgu.csit.inoc.deansoffice.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: MesheryakovAV
 * Date: 28.02.11
 * Time: 11:23
 */
public class EnRegister {
    private List<Order> orders = new ArrayList<Order>();
    private EnrollmentOrder currentOrder;
    private Directive currentDirective;
    private Order.OrderData currentOrderData;
    private List<Employee> leaders = new ArrayList<Employee>();
    private List<String> allCoordinators = new ArrayList<String>();
    private List<Student> students = new ArrayList<Student>();
    private DirectiveProcessor directiveProcessor = new DirectiveProcessor();

    public EnRegister(List<Employee> leaders) {
        allCoordinators.add("Проректор по учебно-организационной работе");
        allCoordinators.add("Начальник учебного управления");
        allCoordinators.add("Начальник юридического отдела");
        allCoordinators.add("Декан факультета");
        allCoordinators.add("Начальник общего отдела");

        this.leaders = leaders;
    }

    public void createNewOrder(String number, Date enrolmentDate, Date signedDate, Date releaseDate) {
        currentOrder = new EnrollmentOrder();
        currentOrder.setNumber(number);
        currentOrder.setEnrollmentDate(enrolmentDate);
        currentOrder.setSignedDate(signedDate);
        currentOrder.setReleaseDate(releaseDate);
        currentOrderData = new Order.OrderData();
    }

    public void enterOrderData(Order.OrderData data) {
        if (currentOrder == null) {
            throw new RuntimeException("Please create order before enter data " +
                    "(call createNewOrder method).");
        }
        currentOrder.setData(data);
    }

    public void enterOrderNote(String note) {
        if (currentOrder == null) {
            throw new RuntimeException("Please create order before enter data " +
                    "(call createNewOrder method).");
        }
        currentOrderData.setNote(note);
        currentOrder.setData(currentOrderData);
    }

    public boolean endCreateOrder() {
        if (currentOrder.getData() != null && currentOrder.getData().getSupervisor() != null
                && students.size() != 0) {
            currentOrder.setState(Order.OrderState.IN_PROCESS);
            orders.add(currentOrder);

            return true;
        } else return false;
    }

    public void approveOrder() {
        for (Order order : orders) {
            if (order.getState().equals(Order.OrderState.IN_PROCESS)) {

                order.setState(Order.OrderState.COMPLETED);
                break;
            }
        }
        students.clear();
    }

    public EnrollmentOrder getCurrentOrder() {
        return currentOrder;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Employee> getLeaders() {
        return leaders;
    }

    public List<String> getAllCoordinators() {
        return allCoordinators;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Order.OrderData getCurrentOrderData() {
        return currentOrderData;
    }
}
