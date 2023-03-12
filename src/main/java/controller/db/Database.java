package controller.db;

import controller.constants.WebConstants;
import org.nampython.base.BaseHttp;
import org.nampython.base.Controller;
import org.nampython.base.HttpRequest;
import org.nampython.base.HttpResponse;

import java.util.Map;

@Controller("/database")
public class Database extends BaseHttp {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        final Map<String, Object> session = request.getSession().getAttributes();
        if (!session.containsKey(WebConstants.USERNAME_SESSION_ID)) {
            response.sendRedirect(super.createRoute("/login"));
            return;
        }

        if (!session.containsKey(WebConstants.DB_PROPERTIES_SESSION_ID) || session.get(WebConstants.DB_CONNECTION_SESSION_ID) == null) {
            response.sendRedirect(super.createRoute("/db-settings.html"));
            return;
        }

        response.sendRedirect(super.createRoute("/db-panel.html"));
//        response.setContent(new Gson().toJson(session.get(WebConstants.DB_PROPERTIES_SESSION_ID)).getBytes());
    }
}
