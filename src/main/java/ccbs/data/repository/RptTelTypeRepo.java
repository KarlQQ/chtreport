package ccbs.data.repository;

import ccbs.data.entity.RptTelType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RptTelTypeRepo extends JpaRepository<RptTelType, String> {
  public List<RptTelType> findByRptTelType(String rptTelType);
}
