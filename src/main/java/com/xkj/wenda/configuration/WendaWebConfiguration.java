package com.xkj.wenda.configuration;
import com.xkj.wenda.interceptor.LoginRequiredInterceptor;
import com.xkj.wenda.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册拦截器
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;
    /*
    将拦截器加载到链路中
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        //这个拦截器必须在后面，因为需要上一个拦截器的hostholder变量
        //在访问/user/**这个页面时使用拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/**");
        super.addInterceptors(registry);
    }
}
