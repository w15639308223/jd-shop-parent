package com.baidu.shop.filter;

import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.status.HttpStatus;
import com.baidu.shop.utils.CookieUtils;
import com.baidu.shop.utils.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/16 10:07
 */
@Slf4j
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtConfig jwtConfig;

   // private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取请求的url
        String requestURI = request.getRequestURI();
        //当前请求如果不在白名单内则开启拦截器
        log.debug("==========" + requestURI);
        //如果当前请求是登录请求,不执行拦截器
        return !jwtConfig.getExcludePath().contains(requestURI);
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        log.info("拦截到请求"+request.getRequestURI());

        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());
        log.info("token信息",token);
        if (token != null){
            //校验
            try {
                // 通过公钥解密，如果成功，就放行，失败就拦截
                JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            } catch (Exception e) {
                log.info("解析失败 拦截"+token);
                // 校验出现异常，返回403
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(HttpStatus.VERITY_ERROR);
            }
        }else{
            log.info("没有token");
            // 校验出现异常，返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.VERITY_ERROR);
        }
        return null;
    }
}
