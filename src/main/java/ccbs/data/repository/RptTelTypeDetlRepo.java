package ccbs.data.repository;

import ccbs.data.entity.RptTelTypeDetl;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RptTelTypeDetlRepo extends JpaRepository<RptTelTypeDetl, String> {
  public Optional<RptTelTypeDetl> getByBillOpidRule(String billOpidRule);
}
