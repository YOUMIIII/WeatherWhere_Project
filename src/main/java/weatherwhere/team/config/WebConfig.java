package weatherwhere.team.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import weatherwhere.team.web.login.intercepter.LogInterceptor;
import weatherwhere.team.web.login.intercepter.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view 에서 접근할 경로
    private String savePath = "file:///C:/springboot_img/"; // 실제 파일 저장 경로(win)
//    private String savePath = "file:///Users/사용자이름/springboot_img/"; // 실제 파일 저장 경로(mac)

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LogInterceptor()) //인터셉터 등록
                .order(1) //인터셉터 호출 순서. 낮을수록 먼저 호출
                .addPathPatterns("/**") //인터셉터 적용할 URL 패턴 지정
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/img/**", "/js/**"); //인터셉터에서 제외할 패턴 지정

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/members/join", "/logout", "/css/**", "/*.ico", "/error", "/region/insert", "/img/**", "/js/**","/region/weather/**","/region/api");
    }
}