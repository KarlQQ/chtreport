package ccbs.conf.base;

// import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages = {"ccbs.dao.core.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory", nameGenerator = PackageBeanNameGenerator.class)
@Profile("local")
public class LocalDataSourceConfig {
    @Value("${ds.username}")
    private String username;
    @Value("${ds.password}")
    private String password;
    @Value("${ds.url}")
    private String url;

//    @Value("${ds.jndi-name}")
//    private String jndiName;


    @Bean("dataSource")
    public DataSource dataSource() throws SQLException {

        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setURL(url);
        return dataSource;

//        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
//        return lookup.getDataSource(jndiName);
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource ds) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(ds);
        return bean.getObject();
    }

}
