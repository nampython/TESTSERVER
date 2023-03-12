package controller.security;


import org.nampython.base.BaseHttp;
import org.nampython.base.Controller;
import org.nampython.base.HttpRequest;
import org.nampython.base.HttpResponse;

@Controller("/logout")
public class LogoutSolet extends BaseHttp {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        request.getSession().invalidate();
        response.sendRedirect(super.createRoute("/"));
    }
}