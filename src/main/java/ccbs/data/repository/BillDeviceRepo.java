package ccbs.data.repository;

import ccbs.data.entity.BillDevice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDeviceRepo extends JpaRepository<BillDevice, String> {
  List<BillDevice> findByBillTela(String billTela);
}
