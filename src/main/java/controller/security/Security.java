package controller.security;

import controller.constants.WebConstants;
import org.nampython.base.BaseHttp;
import org.nampython.base.Controller;
import org.nampython.base.HttpRequest;
import org.nampython.base.HttpResponse;

@Controller("/secured")
public class Security extends BaseHttp {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.addHeader("Content-Type", "application/json");
        response.setContent((request.getSession().getAttributes().containsKey(WebConstants.USERNAME_SESSION_ID) + ""));
    }
}
