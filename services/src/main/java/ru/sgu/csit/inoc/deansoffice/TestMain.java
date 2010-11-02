package ru.sgu.csit.inoc.deansoffice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.sgu.csit.inoc.deansoffice.dao.*;
import ru.sgu.csit.inoc.deansoffice.domain.*;

import java.util.*;

/**
 * .
 * User: hd (KhurtinDN(a)gmail.com)
 * Date: Sep 10, 2010
 * Time: 11:14:30 AM
 */
public class TestMain {
    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");

    private StudentDAO studentDAO = applicationContext.getBean(StudentDAO.class);
    private GroupDAO groupDAO = applicationContext.getBean(GroupDAO.class);
    private SpecialityDAO specialityDAO = applicationContext.getBean(SpecialityDAO.class);
    private FacultyDAO facultyDAO = applicationContext.getBean(FacultyDAO.class);
    private DeanDAO deanDAO = applicationContext.getBean(DeanDAO.class);
    private RectorDAO rectorDAO = applicationContext.getBean(RectorDAO.class);
    private EnrollmentOrderDAO enrollmentOrderDAO = applicationContext.getBean(EnrollmentOrderDAO.class);

    public static void main(String[] args) {
        TestMain testMain = new TestMain();

        testMain.createFaculty();
        testMain.createSpecialities();
        testMain.createGroups();
        testMain.createStudents();
    }

    private void createFaculty() {
        Dean dean = new Dean();
        dean.setFirstName("Антонина");
        dean.setMiddleName("Гавриловна");
        dean.setLastName("Федорова");
        deanDAO.save(dean);

        Rector rector = new Rector();
        rector.setFirstName("Леонид");
        rector.setMiddleName("Юрьевич");
        rector.setLastName("Коссович");
        rector.setDegree("д.ф.-м.н., профессор");
        rectorDAO.save(rector);

        Faculty faculty = new Faculty();
        faculty.setFullName("Компьютерных наук и информационных технологий");
        faculty.setShortName("КНиИТ");
        faculty.setDean(dean);
        faculty.setRector(rector);
        faculty.setCourseCount(6);
        facultyDAO.save(faculty);
    }

    private void createSpecialities() {
        List<Faculty> facultyList = facultyDAO.findAll();

        for (Faculty faculty : facultyList) {

            Speciality speciality = new Speciality();
            speciality.setName("Прикладная математика и информатика");
            speciality.setShortName("ПМИ");
            speciality.setCode("1");
            speciality.setFaculty(faculty);
            specialityDAO.save(speciality);

            speciality = new Speciality();
            speciality.setName("Вычислительные машины, комплексы, системы, сети");
            speciality.setShortName("ВМ");
            speciality.setCode("2");
            speciality.setFaculty(faculty);
            specialityDAO.save(speciality);

            speciality = new Speciality();
            speciality.setName("Компьютерная безопасность");
            speciality.setShortName("КБ");
            speciality.setCode("3");
            speciality.setFaculty(faculty);
            specialityDAO.save(speciality);
        }
    }

    private void createGroups() {
        List<Speciality> specialityList = specialityDAO.findAll();
        for (Speciality speciality : specialityList) {
            int courseCount = speciality.getFaculty().getCourseCount();
            for (int groupCount = 1; groupCount <= 2; ++groupCount) {
                for (int course = 1; course <= courseCount; ++course) {
                    Group group = new Group();
                    group.setName(course + speciality.getCode() + groupCount);
                    group.setCourse(course);
                    group.setSpeciality(speciality);
                    groupDAO.save(group);
                }
            }
        }
    }

    private void createStudents() {
        List<Group> groupList = groupDAO.findAll();

        List<EnrollmentOrder> orders = new ArrayList<EnrollmentOrder>();
        Integer courseCount = 0;
        if (!groupList.isEmpty()) {
            courseCount = groupList.get(0).getSpeciality().getFaculty().getCourseCount();
        }
        for (int i = 0; i < courseCount; ++i) {
            orders.add(StudentGenerator.getRandomEnrollmentOrder(i + 1));
            enrollmentOrderDAO.save(orders.get(orders.size() - 1));
        }

        for (Group group : groupList) {
            for (int studentCount = 1; studentCount <= 20; ++studentCount) {
                Student student = StudentGenerator.getRandomStudent();

                student.setCource(group.getCourse());
                student.setGroup(group);
                student.setSpeciality(group.getSpeciality());
                student.setEnrollmentOrder(orders.get(group.getCourse() - 1));

                studentDAO.save(student);
            }
        }
    }

    private static class StudentGenerator {
        private static Random generator = new Random(new Date().getTime());

        private static String[][] lastNames = {{"Иванов", "Петров", "Захаров", "Клочко", "Кузнецов", "Алексеев", "Свиридов",
                "Яковлев", "Нечаев", "Куприн", "Снегов", "Дубровский", "Шефер", "Привалов", "Горбовский", "Печкин"},
                {"Иванова", "Петрова", "Захарова", "Клочко", "Кузнецова", "Алексеева", "Свиридова",
                        "Яковлева", "Нечаева", "Куприна", "Снегова", "Дубровская", "Шефер", "Привалова", "Горбовская", "Печкина"}};
        private static String[][] lastNamesDative = {{"Иванову", "Петрову", "Захарову", "Клочко", "Кузнецову", "Алексееву", "Свиридову",
                "Яковлеву", "Нечаеву", "Куприну", "Снегову", "Дубровскому", "Шефер", "Привалову", "Горбовскому", "Печкину"},
                {"Ивановой", "Петровой", "Захаровой", "Клочко", "Кузнецовой", "Алексеевой", "Свиридовой",
                        "Яковлевой", "Нечаевой", "Куприной", "Снеговой", "Дубровской", "Шефер", "Приваловой", "Горбовской", "Печкиной"}};

        private static String[][] firstNames = {{"Иван", "Пётр", "Александр", "Семён", "Николай", "Алексей", "Юрий",
                "Борис", "Сергей", "Андрей", "Олег", "Степан", "Максим", "Дмитрий", "Владимир", "Виктор", "Денис"},
                {"Светлана", "Екатерина", "Александра", "Надежда", "Анна", "Юлия", "Томара", "Наталия",
                        "Елена", "Мария", "Марина", "Ольга", "Зульфия", "Алёна", "Лидия", "Антонина", "Анастасия", "Виктория"}};
        private static String[][] firstNamesDative = {{"Ивану", "Петру", "Александру", "Семёну", "Николаю", "Алексею", "Юрию",
                "Борису", "Сергею", "Андрею", "Олегу", "Степану", "Максиму", "Дмитрию", "Владимиру", "Виктору", "Денису"},
                {"Светлане", "Екатерине", "Александре", "Надежде", "Анне", "Юлии", "Томаре", "Наталии",
                        "Елене", "Марии", "Марине", "Ольге", "Зульфие", "Алёне", "Лидии", "Антонине", "Анастасии", "Виктории"}};

        private static String[][] middleNames = {{"Иванович", "Петрович", "Александрович", "Семёнович", "Николаевич", "Алексеевич",
                "Юрьевич", "Борисович", "Сергеевич", "Андреевич", "Олегович", "Степанович", "Максимович", "Дмитриевич",
                "Владимирович", "Викторович", "Денисович"},
                {"Ивановна", "Петровна", "Александровна", "Семёновна", "Николаевна", "Алексеевна",
                        "Юрьевна", "Борисовна", "Сергеевна", "Андреевна", "Олеговна", "Степановна", "Максимовна", "Дмитриевна",
                        "Владимировна", "Викторовна", "Денисовна"}};
        private static String[][] middleNamesDative = {{"Ивановичу", "Петровичу", "Александровичу", "Семёновичу", "Николаевичу", "Алексеевичу",
                "Юрьевичу", "Борисовичу", "Сергеевичу", "Андреевичу", "Олеговичу", "Степановичу", "Максимовичу", "Дмитриевичу",
                "Владимировичу", "Викторовичу", "Денисовичу"},
                {"Ивановне", "Петровне", "Александровне", "Семёновне", "Николаевне", "Алексеевне",
                        "Юрьевне", "Борисовне", "Сергеевне", "Андреевне", "Олеговне", "Степановне", "Максимовне", "Дмитриевне",
                        "Владимировне", "Викторовне", "Денисовне"}};

        public StudentGenerator() {
        }

        public static EnrollmentOrder getRandomEnrollmentOrder(int course) {
            EnrollmentOrder enrolOrder = new EnrollmentOrder();

            enrolOrder.setNumber("22-" + generator.nextInt(10) + generator.nextInt(10)
                    + generator.nextInt(10) + generator.nextInt(10));
            enrolOrder.setSignedDate(new GregorianCalendar().getTime());
            enrolOrder.setEnrollmentDate(new GregorianCalendar(2011 - course, Calendar.SEPTEMBER, 1).getTime());
            enrolOrder.setReleaseDate(new GregorianCalendar(2016 - course, Calendar.JULY, 1).getTime());

            return enrolOrder;
        }


        public static Student getRandomStudent() {
            Student student = new Student();
            int sex = generator.nextInt(2);
            Integer nameIndex = generator.nextInt(firstNames[sex].length);

            student.setFirstName(firstNames[sex][nameIndex]);
            student.setFirstNameDative(firstNamesDative[sex][nameIndex]);

            nameIndex = generator.nextInt(lastNames[sex].length);
            student.setLastName(lastNames[sex][nameIndex]);
            student.setLastNameDative(lastNamesDative[sex][nameIndex]);

            nameIndex = generator.nextInt(middleNames[sex].length);
            student.setMiddleName(middleNames[sex][nameIndex]);
            student.setMiddleNameDative(middleNamesDative[sex][nameIndex]);

            student.setStudyForm(getRandomStudyForm());
            student.setStudentIdNumber("" + ((int) (Math.random() * 100000)));
            student.setDivision(Student.Division.INTRAMURAL);

            return student;
        }

        private static Student.StudyForm getRandomStudyForm() {
            switch (generator.nextInt(3)) {
                case 1:
                    return Student.StudyForm.COMMERCIAL;
            }
            return Student.StudyForm.BUDGET;
        }        
    }
}