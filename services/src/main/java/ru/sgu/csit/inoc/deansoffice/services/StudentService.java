package ru.sgu.csit.inoc.deansoffice.services;

import ru.sgu.csit.inoc.deansoffice.domain.Student;

import java.util.List;

/**
 * .
 * User: hd (KhurtinDN(a)gmail.com)
 * Date: Sep 11, 2010
 * Time: 12:50:03 PM
 */
public interface StudentService {
    boolean equalsFullName(Student student1, Student student2);
    void sortByFullName(List<Student> students);
}
