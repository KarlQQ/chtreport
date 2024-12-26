package ccbs.data.repository;

import ccbs.data.entity.BillRels;
import ccbs.data.entity.BillRels.CompositeKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRelsRepo extends JpaRepository<BillRels, CompositeKey> {
  public List<BillRels> findBySubTela(String subTela);
}
