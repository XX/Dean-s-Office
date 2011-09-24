package ru.sgu.csit.inoc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.sgu.csit.inoc.deansoffice.aos.EnRegister;
import ru.sgu.csit.inoc.deansoffice.dao.*;
import ru.sgu.csit.inoc.deansoffice.domain.Employee;
import ru.sgu.csit.inoc.deansoffice.domain.EnrollmentOrder;
import ru.sgu.csit.inoc.deansoffice.domain.Order;
import ru.sgu.csit.inoc.deansoffice.domain.Student;
import ru.sgu.csit.inoc.deansoffice.services.impl.StudentServiceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class AddEnOrderMain {
    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");

    private static StudentDAO studentDAO = applicationContext.getBean(StudentDAO.class);
    private static GroupDAO groupDAO = applicationContext.getBean(GroupDAO.class);
    private static StipendDAO stipendDAO = applicationContext.getBean(StipendDAO.class);
    private static EmployeeDAO employeeDAO = applicationContext.getBean(EmployeeDAO.class);
    private static EnrollmentOrderDAO orderDAO = applicationContext.getBean(EnrollmentOrderDAO.class);

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.loop();
    }

    public static class Shell {
        private static final String ERROR_PREFIX = "[E]";

        private static Scanner scanner = new Scanner(System.in);

        private static List<Employee> leaders = new ArrayList<Employee>();
        private static EnRegister register;

        static {
            leaders = employeeDAO.findAll();
            register = new EnRegister(leaders);
        }

        public void loop() {
            print(Mode.INTRO);
            while (process(input())) ;
        }

        static int state = 0;

        private boolean process(String command) {
            if ("help".equalsIgnoreCase(command) || "h".equalsIgnoreCase(command)) {
                print(Mode.ALL_COMMANDS);
            } else if ("exit".equals(command) || "quit".equals(command)) {
                print(Mode.END);
                return false;
            } else if ("reset".equals(command)) {
                println("Состояние установлено в начальное");
                state = 0;
            } else if ("create".equals(command)) {
                print("Введите номер приказа: ");
                String number = scanner.nextLine();
                println("Введите дату зачисления");
                print("День: ");
                int day = scanner.nextInt();
                print("Месяц: ");
                int month = scanner.nextInt();
                print("Год: ");
                int year = scanner.nextInt();
                Calendar enrollmentCalendar = new GregorianCalendar(year, month - 1, day);
                println("Введите дату подписи приказа");
                print("День: ");
                day = scanner.nextInt();
                print("Месяц: ");
                month = scanner.nextInt();
                print("Год: ");
                year = scanner.nextInt();
                Calendar signedCalendar = new GregorianCalendar(year, month - 1, day);
                println("Введите предполагаемую дату выпуска");
                print("День: ");
                day = scanner.nextInt();
                print("Месяц: ");
                month = scanner.nextInt();
                print("Год: ");
                year = scanner.nextInt();
                Calendar releaseCalendar = new GregorianCalendar(year, month - 1, day);
                register.createNewOrder(number,
                        enrollmentCalendar.getTime(),
                        signedCalendar.getTime(),
                        releaseCalendar.getTime());
                print(Mode.CREATED_ORDER);
                state = 1;
            } else if ("print".equals(command)) {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(new File("test.pdf"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if ("edit".equals(command)) {
                if (register.getCurrentOrder() != null
                        && register.getCurrentOrder().getState() == Order.OrderState.CREATED) {
                    print(Mode.ORDER_INFO);
                    state = 1;
                }
                state = 1;
            } else if (state == 1) {
                if ("1".equals(command)) {
                    println("Введите текст примечания:");
//                    register.getCurrentOrderData().setNote(input());
//                    register.enterOrderData(register.getCurrentOrderData());
                    register.enterOrderNote(input());
                    print(Mode.UPDATED_ORDER);
                } else if ("2".equals(command)) {
                    println("stlist group=XXX,XXX - вывод информации о студентах,");
                    println("stadd group=XXX,XXX id,id - добавление студентов из группы и по id,");
                    println("stremove id,id - удаление студентов по id.");
//                    state = 2;
                } else if (command.startsWith("stlist ")) {
                    Scanner strScanner = new Scanner(command);
                    strScanner.useDelimiter("[ ]+");
                    Pattern pattern = Pattern.compile("[a-zA-Z=,[0-9]]+");
                    while (strScanner.hasNext(pattern)) {
                        String str = strScanner.next(pattern);
                        if (!str.startsWith("stlist")) {
                            int assignIndex = str.lastIndexOf("=");
                            String param = str.substring(assignIndex + 1, str.length());

                            if (str.startsWith("group=")) {
                                Scanner paramScanner = new Scanner(param);
                                paramScanner.useDelimiter(",");
                                Pattern paramPattern = Pattern.compile("[0-9]+");
                                while (paramScanner.hasNext(paramPattern)) {
                                    String groupName = paramScanner.next(paramPattern);
                                    List<Student> students = studentDAO.findByGroup(
                                            groupDAO.findByName(groupName).get(0));

                                    (new StudentServiceImpl()).sortByFullName(students);
                                    println("=== Группа " + groupName + " ===");
//                                    groupNames.add(groupName);
                                    for (Student student : students) {
                                        println(student);
                                    }
                                }
                            }
                        }
                    }
//                    println("stlist course=x,x group=XXX,XXX - вывод информации о студентах,");
//                    println("stadd id-id,id - добавление студентов по id,");
//                    println("stremove id-id,id - удаление студентов по id.");
//                    state = 2;
                } else if (command.startsWith("stadd ")) {
                    Scanner strScanner = new Scanner(command);
                    strScanner.useDelimiter("[ ]+");
                    Pattern pattern = Pattern.compile("[a-zA-Z=,[0-9]]+");
                    while (strScanner.hasNext(pattern)) {
                        String str = strScanner.next(pattern);
                        if (!str.startsWith("stadd")) {
                            int assignIndex = str.lastIndexOf("=");
                            String param = str.substring(assignIndex + 1, str.length());

                            if (str.startsWith("group=")) {
                                Scanner paramScanner = new Scanner(param);
                                paramScanner.useDelimiter(",");
                                Pattern paramPattern = Pattern.compile("[0-9]+");
                                while (paramScanner.hasNext(paramPattern)) {
                                    String groupName = paramScanner.next(paramPattern);
                                    List<Student> students = studentDAO.findByGroup(
                                            groupDAO.findByName(groupName).get(0));
                                    for (Student student : students) {
                                        register.getStudents().add(student);
                                    }
                                }
                            } else {
                                Scanner paramScanner = new Scanner(param);
                                paramScanner.useDelimiter(",");
                                Pattern paramPattern = Pattern.compile("[0-9]+");
                                while (paramScanner.hasNext(paramPattern)) {
                                    Long id = Long.valueOf(paramScanner.next(paramPattern));
                                    Student student = studentDAO.findById(id);
                                    register.getStudents().add(student);
                                }
                            }
                            (new StudentServiceImpl()).sortByFullName(register.getStudents());
                        }
                    }
                    print(Mode.UPDATED_ORDER);
                } else if (command.startsWith("stremove ")) {
                    Scanner strScanner = new Scanner(command);
                    strScanner.useDelimiter("[ ]+");
                    Pattern pattern = Pattern.compile("[a-zA-Z=,[0-9]]+");
                    while (strScanner.hasNext(pattern)) {
                        String str = strScanner.next(pattern);
                        if (!str.startsWith("stremove")) {
                            String param = str;
                            Scanner paramScanner = new Scanner(param);
                            paramScanner.useDelimiter(",");
                            Pattern paramPattern = Pattern.compile("[0-9]+");
                            while (paramScanner.hasNext(paramPattern)) {
                                Long id = Long.valueOf(paramScanner.next(paramPattern));
                                for (int i = 0; i < register.getStudents().size(); ++i) {
                                    Student student = register.getStudents().get(i);
                                    if (id.equals(student.getId())) {
                                        register.getStudents().remove(i);
                                    }
                                }
                            }
                        }
                    }
                    print(Mode.UPDATED_ORDER);
                } else if ("3".equals(command)) {
                    println("Выберите супервизора:");
                    for (int i = 0; i < leaders.size(); ++i) {
                        Employee leader = leaders.get(i);

                        println((i + 1) + ". " + leader.getPosition() + ", " + leader.getDegree() + " - "
                                + leader.generateShortName(true));
                    }
                    state = 3;
                } else if ("4".equals(command)) {
                    for (String position : register.getAllCoordinators()) {
                        Employee coordinator = new Employee();
                        coordinator.setPosition(position);

                        register.getCurrentOrderData().addCoordinator(coordinator);
                    }
                    register.enterOrderData(register.getCurrentOrderData());
                    println("Выбраны все доступные координаторы.");
                    print(Mode.UPDATED_ORDER);
                } else if ("approve".equals(command)) {
                    if (register.endCreateOrder()) {
                        for (Student student : register.getStudents()) {
                            orderDAO.saveOrUpdate(register.getCurrentOrder());
                            student.setEnrollmentOrder(register.getCurrentOrder());
                            studentDAO.update(student);
                        }
                        register.approveOrder();
                    }
                } else {
                    print(Mode.UNKNOWN_COMMAND);
                }
            } else if (state == 2) {
                try {
                    int index = Integer.valueOf(command) - 1;

                    print(Mode.UPDATED_ORDER);
                    state = 1;
                } catch (NumberFormatException e) {
                    print(Mode.UNKNOWN_COMMAND);
                } catch (ArrayIndexOutOfBoundsException e) {
                    println(ERROR_PREFIX + "Нет директивы с таким номером");
                }
            } else if (state == 3) {
                try {
                    int index = Integer.valueOf(command) - 1;
                    Employee leader = leaders.get(index);

                    register.getCurrentOrderData().setSupervisor(leader);
                    register.enterOrderData(register.getCurrentOrderData());
                    print(Mode.UPDATED_ORDER);
                    state = 1;
                } catch (NumberFormatException e) {
                    print(Mode.UNKNOWN_COMMAND);
                } catch (ArrayIndexOutOfBoundsException e) {
                    println(ERROR_PREFIX + "Нет супервизора с таким номером");
                }
            } else {
                print(Mode.UNKNOWN_COMMAND);
            }

            return true;
        }

        private String input() {
            print(Mode.PROMPT);
            return scanner.nextLine();
        }

        private void print(Mode mode) {
            switch (mode) {
                case PROMPT:
                    print("> ");
                    break;
                case INTRO:
                    println("=== Запуск программы ===");
                    break;
                case END:
                    println("=== Конец программы ====");
                    break;
                case UNKNOWN_COMMAND:
                    println(ERROR_PREFIX + " Неизвестная комманда");
                    break;
                case CREATED_ORDER:
                    println("Создан приказ");
                    print(Mode.ORDER_INFO);
                    break;
                case UPDATED_ORDER:
                    println("\nПриказ обновлён");
                    print(Mode.ORDER_INFO);
                    break;
                case ORDER_INFO:
                    EnrollmentOrder order = register.getCurrentOrder();
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

                    println("Состояние: " + order.getState());
                    println("Номер приказа: " + order.getNumber());
                    println("Дата зачисления: " + dateFormat.format(order.getEnrollmentDate()));
                    println("Дата подписания: " + dateFormat.format(order.getSignedDate()));
                    println("Дата выпуска: " + dateFormat.format(order.getReleaseDate()));
                    println("1. Примечание: " +
                            (order.getData() != null && order.getData().getNote() != null
                                    ? order.getData().getNote() : ""));
                    println("2. Студенты:");
                    for (Student student : register.getStudents()) {
                        println(student);
                    }
                    println("3. Супервизор: " +
                            (order.getData() != null && order.getData().getSupervisor() != null
                                    ? order.getData().getSupervisor().generateShortName(true) : ""));
                    println("4. Согласовано:");
                    if (order.getData() != null) {
                        for (Employee coordinator : order.getData().getCoordinators()) {
                            println("  " + coordinator.getPosition());
                        }
                    }
                    break;
                case ALL_COMMANDS:
                    println("\nСписок всех комманд:");
                    println("help, h - вывод справки по командам");
                    println("exit, quit - завершение работы программы");
                    println("");
                    break;
            }
        }

        private void print(String text) {
            System.out.print(text);
        }

        private void println(String text) {
            System.out.println(text);
        }

        private void print(Student student) {
            print(student.getId() + "\t" + student.getLastName() + " " + student.getFirstName() + " " +
                    student.getMiddleName() + "\t" + student.getStudyForm());
        }

        private void println(Student student) {
            print(student);
            println("");
        }

        public static enum Mode {
            PROMPT, INTRO, END, ALL_COMMANDS, UNKNOWN_COMMAND,
            CREATED_ORDER, UPDATED_ORDER, ORDER_INFO
        }

//        public static abstract class Command {
//            public List<String> names = new ArrayList<String>();
//
//            public abstract int action();
//            public abstract boolean isThis();
//
//            public List<String> getNames() {
//                return names;
//            }
//
//            public void setNames(List<String> names) {
//                this.names = names;
//            }
//
//            public void addName(String name) {
//                this.names.add(name);
//            }
//
//            public boolean is(String name);
//        }
//
//        public static class CommandHelp extends Command {
//            public CommandHelp() {
//                names.add("help");
//                names.add("h");
//            }
//
//            @Override
//            public int action() {
//                return 0;
//            }
//
//            @Override
//            public boolean isThis() {
//                return false;
//            }
//        }
    }
}
