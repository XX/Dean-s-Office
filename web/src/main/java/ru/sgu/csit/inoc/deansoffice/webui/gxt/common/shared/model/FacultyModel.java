package ru.sgu.csit.inoc.deansoffice.webui.gxt.common.shared.model;

/**
 * User: Khurtin Denis ( KhurtinDN (a) gmail.com )
 * Date: 2/10/11
 * Time: 12:42 PM
 */
public class FacultyModel extends DtoModel {
    private PersonModel dean;

    public FacultyModel() {
    }

    public FacultyModel(Long id) {
        super(id);
    }

    public String getFullName() {
        return get("fullName");
    }

    public void setFullName(String fullName) {
        set("fullName", fullName);
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public PersonModel getDean() {
        return dean;
    }

    public void setDean(PersonModel dean) {
        this.dean = dean;
    }

    public Integer getCourseCount() {
        return get("courseCount");
    }

    public void setCourseCount(Integer courseCount) {
        set("courseCount", courseCount);
    }

}