

package com.example.patterns.config;

import com.example.common.tools.ApplicationContextHolder;
import com.example.patterns.chain.AbstractChainContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设计模式自动装配
 *

 */
@Configuration
public class DesignPatternAutoConfiguration {


    /**
     * 责任链上下文
     */
    @Bean
    public AbstractChainContext abstractChainContext() {
        return new AbstractChainContext();
    }
}
