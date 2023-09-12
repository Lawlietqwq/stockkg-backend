package com.example.web.tools;

import com.example.common.exception.CommonException;
import com.example.common.result.ResultTypeEnum;
import com.example.common.tools.JsonUtil;
import com.example.web.annotations.RepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 重复提交拦截器
 */
@Component
@Slf4j
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    /**
     * 请求承诺书
     */
    private static final String REPEAT_PARAMETERS = "repeatParameters";
    /**
     * 请求时间
     */
    private static final String REPEAT_TIME = "repeatTime";
    /**
     * 请求间隔小于10s才处理
     */
    private static final int REPEAT_TIME_INTERVAL = 10;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        if (o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            RepeatSubmit repeatSubmit = handlerMethod.getMethodAnnotation(RepeatSubmit.class);
            if (repeatSubmit == null) {
                return true;
            }
            String parameterMap = JsonUtil.objToJson(request.getParameterMap());
            Map<String, Object> nowData = new HashMap<>(8);
            nowData.put(REPEAT_PARAMETERS, parameterMap);
            long nowTime = System.currentTimeMillis();
            nowData.put(REPEAT_TIME, nowTime);

            String requestURI = request.getRequestURI();

            HttpSession session = request.getSession();
            Object repeatData = session.getAttribute("repeatData");
            if (repeatData != null) {
                Map<String, Object> sessionData = (Map<String, Object>) repeatData;
                if (sessionData.containsKey(requestURI)) {
                    Map<String, Object> oldData = (Map<String, Object>) sessionData.get(requestURI);
                    long oldTime = (Long) oldData.get(REPEAT_TIME);
                    String oldParameterMap = (String) oldData.get(REPEAT_PARAMETERS);
                    if (parameterMap.equals(oldParameterMap) && (nowTime - oldTime) / 1000 < REPEAT_TIME_INTERVAL) {
                        log.warn("请勿重复提交");
                        CommonException.fail(ResultTypeEnum.REPEAT_ERROR);
                        return false;
                    }
                }
            }
            Map<String, Object> newSessionData = new HashMap<>();
            newSessionData.put(requestURI, nowData);
            session.setAttribute("repeatData", newSessionData);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
