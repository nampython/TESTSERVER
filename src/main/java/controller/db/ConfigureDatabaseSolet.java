package controller.db;

import com.mysql.jdbc.Driver;
import controller.constants.WebConstants;
import model.DbProperties;
import org.nampython.base.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

@Controller("/database/configure")
public class ConfigureDatabaseSolet extends BaseHttp {
    private SoletLogger loggingService;

    @Override
    public void init(SoletConfig soletConfig) {
        super.init(soletConfig);
        this.loggingService = (SoletLogger) soletConfig.getAttribute(SoletConstants.SOLET_CONFIG_LOGGER);

        //Initialize driver
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException e) {
            this.loggingService.printStackTrace(e);
        }
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        final Map<String, Object> session = request.getSession().getAttributes();
        if (!session.containsKey(WebConstants.USERNAME_SESSION_ID)) {
            response.sendRedirect(super.createRoute("/login"));
            return;
        }

        final Map<String, String> params = request.getBodyParameters();
        final DbProperties properties = new DbProperties(
                params.get("server"),
                params.get("port"),
                params.get("user"),
                params.get("password"),
                params.get("database")
        );

        session.put(WebConstants.DB_PROPERTIES_SESSION_ID, properties);

        if (session.get(WebConstants.DB_CONNECTION_SESSION_ID) != null) {
            try {
                ((Connection) session.get(WebConstants.DB_CONNECTION_SESSION_ID)).close();
                session.remove(WebConstants.DB_CONNECTION_SESSION_ID);
                this.loggingService.info("Closed previous connection");
            } catch (SQLException ex) {
                this.loggingService.printStackTrace(ex);
            }
        }

        try {
            final Properties authProps = new Properties();
            authProps.setProperty("user", properties.username);
            if (!properties.password.trim().equals("")) {
                authProps.setProperty("password", properties.password);
            }

            final Connection connection = DriverManager
                    .getConnection(String.format("jdbc:mysql://%s:%s/%s?useSSL=false&createDatabaseIfNotExist=true",
                            properties.server,
                            properties.port,
                            properties.database
                    ), authProps);

            connection.prepareStatement(" CREATE TABLE IF NOT EXISTS entries (\n" +
                    "\tid INT(11) AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    entry_name VARCHAR(255) NOT NULL\n" +
                    " );\n" +
                    " ").execute();

            session.put(WebConstants.DB_CONNECTION_SESSION_ID, connection);

        } catch (Exception e) {
            this.loggingService.printStackTrace(e);
            response.sendRedirect(super.createRoute("/db-settings.html"));
            return;
        }

        response.sendRedirect(super.createRoute("/database"));
    }
}
