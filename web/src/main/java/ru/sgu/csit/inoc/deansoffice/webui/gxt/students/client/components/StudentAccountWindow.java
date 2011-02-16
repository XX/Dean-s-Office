package ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.components;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.constants.ErrorCode;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.mvc.events.AppEvents;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.services.StudentService;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.shared.model.StudentDetailsModel;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.shared.utils.StudentModelUtil;

/**
 * User: KhurtinDN ( KhurtinDN@gmail.com )
 * Date: 12/28/10
 * Time: 3:04 PM
 */
public class StudentAccountWindow extends Window {
    private Html headerHtml = new Html();
    private StudentDataForm studentDataForm = new StudentDataForm();
    private StudentDetailsModel studentDetailsModel;

    private StudentDetailsAsyncCallback studentDetailsAsyncCallback = new StudentDetailsAsyncCallback();

    public StudentAccountWindow() {
        setSize(800, 600);
        setModal(true);
        setBlinkModal(true);
        setMaximizable(true);
        setButtonAlign(Style.HorizontalAlignment.CENTER);

        addButton(new Button("Сохранить", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Info.display("Warn!", LocaleInfo.getCurrentLocale().toString());
            }
        }));

        addButton(new Button("Сгенерировать в PDF", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                String url = "../documents/dossier.pdf?studentId=" + studentDetailsModel.getId();
                com.google.gwt.user.client.Window.open(url, "_blank", "");
            }
        }));

        addButton(new Button("Закрыть", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                StudentAccountWindow.this.hide();
            }
        }));
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        setLayout(new FitLayout());

        TabPanel tabPanel = new TabPanel();
        tabPanel.add(createFaceTabItem());
//        tabPanel.add(createBackTabItem());

        add(tabPanel, new FitData(4));
    }

    private TabItem createFaceTabItem() {
        Viewport viewport = new Viewport();
        viewport.setLayout(new VBoxLayout(VBoxLayout.VBoxLayoutAlign.STRETCH));

        headerHtml.setStyleName("studentAccount");
        viewport.add(headerHtml);

        VBoxLayoutData flex = new VBoxLayoutData(10, 0, 0, 0);
        flex.setFlex(1);
        studentDataForm.setScrollMode(Style.Scroll.AUTOY);
        viewport.add(studentDataForm, flex);

        TabItem faceTabItem = new TabItem("Учетная карточка студента");
        faceTabItem.setLayout(new FitLayout());
        faceTabItem.add(viewport);

        return faceTabItem;
    }

    /*private TabItem createBackTabItem() {
        TabItem backTabItem = new TabItem("Задняя сторона");
        backTabItem.addText("Здесь была задняя сторона");
        return backTabItem;
    }*/

    private String getHeaderTemplate() {
        return new StringBuilder()
                .append("<img align=\"left\" width=\"100px\" src=\"photos/{photoId}.jpg\" />")
                .append("<h1>Учетная карточка студента</h1>")
                .append("<div class=\"stFIO\">")
                .append("<span class=\"stLabel\">Фамилия:</span> <span class=\"stValue\">{lastName}</span> ")
                .append("<span class=\"stLabel\">Имя:</span> <span class=\"stValue\">{firstName}</span> ")
                .append("<span class=\"stLabel\">Отчество:</span> <span class=\"stValue\">{middleName}</span>")
                .append("</div>")
                .append("<div class=\"stSpeciality\">")
                .append("<span class=\"stLabel\">Специальность (направление):</span>")
                .append("<span class=\"stValue\">{specialityFullName} ({specialityShortName})</span>")
                .append("<span class=\"stLabel\">Шифр (по ОКСО):</span><span class=\"stValue\">{specialityCode}</span>")
                .append("</div>")
                .toString();
    }

    public void showStudentAccount(Long studentId) {
        StudentService.App.getInstance().loadStudentDetails(studentId, studentDetailsAsyncCallback);
    }

    private void showStudentAccountWindow(StudentDetailsModel studentDetailsModel) {
        this.studentDetailsModel = studentDetailsModel;
        setHeading("Данные студента: " + StudentModelUtil.getFullName(studentDetailsModel));
        XTemplate template = XTemplate.create(getHeaderTemplate());

        prepareStudentDetail(studentDetailsModel);
        headerHtml.setHtml(template.applyTemplate(Util.getJsObject(studentDetailsModel, 1)));

        studentDataForm.addStyleName("studentDataForm");
        studentDataForm.setStudentDetails(studentDetailsModel);

        show();
    }

    private void prepareStudentDetail(StudentDetailsModel studentDetailsModel) {
        studentDetailsModel.set("specialityFullName", studentDetailsModel.getSpeciality().getFullName());
        studentDetailsModel.set("specialityShortName", studentDetailsModel.getSpeciality().getShortName());
        studentDetailsModel.set("specialityCode", studentDetailsModel.getSpeciality().getCode());
    }

    @Override
    public void show() {
        super.show();
        maximize();
        layout(true);
    }

    private class StudentDetailsAsyncCallback implements AsyncCallback<StudentDetailsModel> {

        @Override
        public void onFailure(Throwable caught) {
            AppEvent appEvent = new AppEvent(AppEvents.Error, ErrorCode.ServerReturnError);
            appEvent.setData("throwable", caught);
            Dispatcher.forwardEvent(appEvent);
        }

        @Override
        public void onSuccess(StudentDetailsModel studentDetailsModel) {
            showStudentAccountWindow(studentDetailsModel);
        }
    }
}