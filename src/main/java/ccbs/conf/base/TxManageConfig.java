package ccbs.conf.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement 
public class TxManageConfig implements TransactionManagementConfigurer{

	@Qualifier("dataSource") 
	@Autowired
    private DataSource dataSource;
	
    @Bean(name = "tm")
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() { 
        return txManager(); 
    } 
}
