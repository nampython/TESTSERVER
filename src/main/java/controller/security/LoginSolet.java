package controller.security;


import controller.constants.WebConstants;
import org.nampython.base.BaseHttp;
import org.nampython.base.Controller;
import org.nampython.base.HttpRequest;
import org.nampython.base.HttpResponse;
import org.nampython.base.api.HttpStatus;

@Controller("/login")
public class LoginSolet extends BaseHttp {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if (isSecured(request)) {
            redirectToHome(response);
            return;
        }

        response.setContent("g");
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.sendRedirect(super.createRoute("/login.html"));
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        if (isSecured(request)) {
            redirectToHome(response);
            return;
        }

        String username = request.getBodyParam("username");
        String password = request.getBodyParam("password");

        request.getSession().addAttribute(WebConstants.USERNAME_SESSION_ID, username);
        request.getSession().addAttribute("password", password);

        redirectToHome(response);
    }

    private boolean isSecured(HttpRequest request) {
        return request.getSession().getAttributes().containsKey(WebConstants.USERNAME_SESSION_ID);
    }

    private void redirectToHome(HttpResponse response) {
        response.sendRedirect(super.createRoute("/"));
    }
}
