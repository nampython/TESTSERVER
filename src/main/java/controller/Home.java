package controller;

import org.nampython.base.BaseHttp;
import org.nampython.base.Controller;
import org.nampython.base.HttpRequest;
import org.nampython.base.HttpResponse;

@Controller("/")
public class Home extends BaseHttp {
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.sendRedirect(super.createRoute("/index.html"));
    }
}
