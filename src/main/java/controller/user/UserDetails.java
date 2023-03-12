package controller.user;

import com.google.gson.Gson;
import controller.constants.WebConstants;
import model.User;
import org.nampython.base.BaseHttp;
import org.nampython.base.Controller;
import org.nampython.base.HttpRequest;
import org.nampython.base.HttpResponse;
import org.nampython.base.api.HttpStatus;

import java.util.Map;


@Controller("/logged-user/details")
public class UserDetails extends BaseHttp {
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if (!request.getSession().getAttributes().containsKey(WebConstants.USERNAME_SESSION_ID)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.setContent(" ");
            response.addHeader("Location", "/");
            return;
        }
        response.addHeader("Content-Type", "application/json");
        final Map<String, Object> session = request.getSession().getAttributes();
        final User user = new User(session.get(WebConstants.USERNAME_SESSION_ID).toString(), session.get("password").toString());
        response.setContent(new Gson().toJson(user));
    }
}
