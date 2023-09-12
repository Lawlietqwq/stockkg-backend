

package com.example.web.config;

import com.example.web.annotations.ILog;
import com.example.web.log.ILogPrintAspect;
import org.springframework.context.annotation.Bean;

/**
 * 日志自动装配
 *

 */
public class LogAutoConfiguration {

    /**
     * {@link ILog} 日志打印 AOP 切面
     */
    @Bean
    public ILogPrintAspect iLogPrintAspect() {
        return new ILogPrintAspect();
    }
}
