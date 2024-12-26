package ccbs.conf.base;

// import oracle.jdbc.pool.OracleDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@MapperScan(basePackages = {"ccbs.dao.core.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory",
    nameGenerator = PackageBeanNameGenerator.class)
@Profile({"prd", "tst"})
public class DataSourceConfig {
  //	@Value("${ds.jndi-name}")
  //	private String jndiName;

  @Value("${ds.username}") private String username;
  @Value("${ds.password}") private String password;
  @Value("${ds.url}") private String url;

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
  public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource ds)
      throws Exception {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(ds);
    return bean.getObject();
  }
}
