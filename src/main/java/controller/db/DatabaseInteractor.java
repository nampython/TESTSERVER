package controller.db;

import com.google.gson.Gson;
import controller.constants.WebConstants;
import org.nampython.base.*;
import org.nampython.base.api.HttpStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("/database/interact")
public class DatabaseInteractor extends BaseHttp {

    private SoletLogger loggingService;

    @Override
    public void init(SoletConfig soletConfig) {
        super.init(soletConfig);
        this.loggingService = (SoletLogger) soletConfig.getAttribute(SoletConstants.SOLET_CONFIG_LOGGER);
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if (this.notLoggedOrDbInvalid(request)) {
            response.sendRedirect(super.createRoute("/"));
            return;
        }

        try {
            final Connection connection = (Connection) request.getSession().getAttribute(WebConstants.DB_CONNECTION_SESSION_ID);
            response.setStatusCode(HttpStatus.OK);
            response.addHeader("Content-Type", "application/json");

            final List<String> entries = new ArrayList<>();
            final ResultSet resultSet = connection.prepareStatement("SELECT * FROM entries").executeQuery();
            while (resultSet.next())
                entries.add(resultSet.getString("entry_name"));

            this.loggingService.info(new Gson().toJson(entries));
            response.setContent(new Gson().toJson(entries));
        } catch (Exception ex) {
            this.loggingService.printStackTrace(ex);
            response.sendRedirect(super.createRoute("/"));
        }
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        if (this.notLoggedOrDbInvalid(request)) {
            response.sendRedirect(super.createRoute("/"));
            return;
        }

        try {
            final Connection connection = (Connection) request.getSession().getAttributes().get(WebConstants.DB_CONNECTION_SESSION_ID);
            final String entryName = request.getBodyParameters().get("entryName");
            connection.prepareStatement(String.format("INSERT INTO entries VALUES (null, '%s')", entryName)).execute();

            response.addHeader("Content-Type", "application/json");
            response.setContent("true");
        } catch (Exception ex) {
            this.loggingService.printStackTrace(ex);
            response.sendRedirect(super.createRoute("/"));
        }
    }

    private boolean notLoggedOrDbInvalid(HttpRequest request) {
        Map<String, Object> session = request.getSession().getAttributes();
        return !session.containsKey(WebConstants.USERNAME_SESSION_ID) ||
                !session.containsKey(WebConstants.DB_PROPERTIES_SESSION_ID) ||
                session.get(WebConstants.DB_CONNECTION_SESSION_ID) == null;
    }
}
