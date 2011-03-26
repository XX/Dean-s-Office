package ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.mvc.views;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.View;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.components.orders.OrderDialog;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.components.orders.OrderQueueWindow;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.components.orders.ReferenceQueueWindow;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.mvc.controllers.OrderController;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.client.mvc.events.AppEvents;
import ru.sgu.csit.inoc.deansoffice.webui.gxt.students.shared.model.OrderModel;

/**
 * User: Khurtin Denis ( KhurtinDN (a) gmail.com )
 * Date: 3/9/11
 * Time: 1:07 PM
 */
public class OrderView extends View {
    private ReferenceQueueWindow referenceQueueWindow;
    private OrderQueueWindow orderQueueWindow;
    private OrderDialog orderDialog;

    public OrderView(OrderController controller) {
        super(controller);
    }

    @Override
    protected void initialize() {
        super.initialize();

        referenceQueueWindow = new ReferenceQueueWindow();
        orderQueueWindow = new OrderQueueWindow();
        orderDialog = new OrderDialog();
    }

    @Override
    protected void handleEvent(AppEvent event) {
        EventType eventType = event.getType();

        if (eventType.equals(AppEvents.ReferenceQueueCall)) {
            onReferenceQueueCall(event);
        } else if (eventType.equals(AppEvents.OrderQueueCall)) {
            onOrderQueueCall(event);
        } else if (eventType.equals(AppEvents.AddNewOrderCall)) {
            onAddNewOrderCall(event);
        } else if (eventType.equals(AppEvents.EditOrderCall)) {
            onEditOrderCall(event);
        }
    }

    private void onReferenceQueueCall(AppEvent event) {
        referenceQueueWindow.show();
    }

    private void onOrderQueueCall(AppEvent event) {
        orderQueueWindow.show();
    }

    private void onAddNewOrderCall(AppEvent event) {
        orderDialog.showNewOrderDialog();
    }

    private void onEditOrderCall(AppEvent event) {
        orderDialog.showEditOrderDialog( (OrderModel) event.getData() );
    }
}
