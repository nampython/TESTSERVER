package controller.user;


import controller.constants.WebConstants;
import org.nampython.base.BaseHttp;
import org.nampython.base.Controller;
import org.nampython.base.HttpRequest;
import org.nampython.base.HttpResponse;

@Controller("/logged-user/profile")
public class UserProfile extends BaseHttp {
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if (!request.getSession().getAttributes().containsKey(WebConstants.USERNAME_SESSION_ID)) {
            response.sendRedirect(super.createRoute("/login"));
        } else {
            response.sendRedirect(super.createRoute("/user-details.html"));
        }
    }
}
