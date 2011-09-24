package ru.sgu.csit.inoc.deansoffice.services.impl;

import org.springframework.stereotype.Service;
import ru.sgu.csit.inoc.deansoffice.domain.Student;
import ru.sgu.csit.inoc.deansoffice.services.StudentService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * .
 * User: hd (KhurtinDN(a)gmail.com)
 * Date: Sep 11, 2010
 * Time: 12:50:30 PM
 */
@Service
public class StudentServiceImpl implements StudentService {
    public boolean equalsFullName(Student student1, Student student2) {
        return (student1.getFirstName().compareTo(student2.getFirstName()) == 0 &&
                student1.getMiddleName().compareTo(student2.getMiddleName()) == 0 &&
                student1.getLastName().compareTo(student2.getLastName()) == 0);
    }

    @Override
    public void sortByFullName(List<Student> students) {
        Collections.sort(students, new Comparator<Student>() {
            public int compare(Student firstStudent, Student secondStudent) {
                int result = firstStudent.getLastName().compareTo(secondStudent.getLastName());
                if (result == 0) {
                    result = firstStudent.getFirstName().compareTo(secondStudent.getFirstName());
                }
                if (result == 0) {
                    result = firstStudent.getMiddleName().compareTo(secondStudent.getMiddleName());
                }

                return result;
            }
        });
    }
}
