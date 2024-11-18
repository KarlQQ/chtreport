package ccbs.conf.base;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

// @PropertySource(value = {"classpath:conf/application-${spring.profiles.active}.properties"})
@Configuration
@Import({
        MvcConfig.class,
        OpenApiConfig.class,
        DataSourceConfig.class,
        LocalDataSourceConfig.class,
        TxManageConfig.class,
        AopConfig.class
})
public class AppConfig {

}
