package weatherwhere.team.web.login.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;
import weatherwhere.team.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        return true;

    }

}
