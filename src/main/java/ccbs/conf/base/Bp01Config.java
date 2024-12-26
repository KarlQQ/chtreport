package ccbs.conf.base;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ccbs.bp01")
@Data
public class Bp01Config {
  private Bp01f0003Config bp01f0003;
  private Rqbp019Config rqbp019;
  private Bp01f0013Config bp01f0013;
  private Bp01f0015Config bp01f0015;

  @Bean
  public Bp01f0003Config bp01f0003() {
    return bp01f0003;
  }

  @Bean
  public Rqbp019Config rqbp019() {
    return rqbp019;
  }

  @Bean
  public Bp01f0013Config bp01f0013() {
    return bp01f0013;
  }

  @Bean
  public Bp01f0015Config bp01f0015() {
    return bp01f0015;
  }

  @Data
  public static class Rqbp019Config {
    private String input;
    private List<TypeConfig> typeConfigs;

    public TypeConfig getConfig(String type) {
      return typeConfigs.stream()
          .filter(typeConfig -> typeConfig.name.equals(type))
          .findAny()
          .orElseThrow();
    }

    @Data
    public static class TypeConfig {
      private String name;
      private String pattern;
      private String output;
      private String filename;
    }
  }

  @Data
  public static class Bp01f0015Config {
    private String rptCode;
    private String filename;
    private Years years;
    private Receive receiveItems;

    @Data
    public static class Receive {
      private String headers;
      private List<TypeItem> receiveTypes;
    }
  }

  @Data
  public static class Bp01f0013Config {
    private String rptCode;
    private String filename;
    private Years years;
    private List<String> titles = Collections.emptyList();
  }

  @Data
  public static class Bp01f0003Config {
    private String rptCode;
    private String filename;
    private Years years;
    private List<GroupBillItem> groupBillItems;
    private List<TypeItem> rptCustTypes;

    @Data
    public static class GroupBillItem {
      private String name;
      private String pattern;
      private List<TypeItem> billItemTypes;
    }
  }

  @Data
  public static class TypeItem {
    private String name;
    private String type;
  }

  @Data
  public static class Years {
    private Integer shift = 0;
    private Integer length = 0;
  }
}
