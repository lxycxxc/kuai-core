package cn.lxycx.kuaicore.conf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HandlerConf implements WebMvcConfigurer {


    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor());
        registration.addPathPatterns("/interface/**"); //拦截
        registration.excludePathPatterns("/interface/admin/login.html","/interface/login");
        //registration.excludePathPatterns("/static/**","/login_start.html","/login_start_local.html","/header.html","/400","/400.html","/error","/login","/login_hw","/word/downword","/file/**","/hwobs/findList"); //排除

        //登录鉴权
//        InterceptorRegistration satoken = registry.addInterceptor(new SaInterceptor(handler -> {
//            // 指定一条 match 规则
//            SaRouter.match("/**")    // 拦截的 path 列表，可以写多个 */
//                    .notMatch("/doLogin","/magic/web/**","/conf/**","/css/**","/js/**")        // 排除掉的 path 列表，可以写多个
//                    .check(r ->
//                            StpUtil.checkLogin() //检查是否登录未登录抛出异常
//                    );        // 要执行的校验动作，可以写完整的 lambda 表达式
//
//            // 根据路由划分模块，不同模块不同鉴权
//          /*  SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));*/
//        }));
//        satoken.addPathPatterns("/interface/**");

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        /*configurer.enable();
        configurer.enable("defaultServletName");*/
    }

}
