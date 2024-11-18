package ccbs.data.repository;

import ccbs.data.entity.RptBillMain;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RptBillMainRepo extends JpaRepository<RptBillMain, String> {
  List<RptBillMain> findByBillOffAndBillTelAndBillMonthAndBillId(
      String billOff, String billTel, String billMonth, String billId);

  List<RptBillMain> findByBillOffAndBillTel(String billOff, String billTel);
}
