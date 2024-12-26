package ccbs.data.repository;

import ccbs.data.entity.RptBillMain;
import ccbs.data.entity.RptBillMain.CompositeKey;
import ccbs.data.entity.Rqbp019Type3Dto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RptBillMainRepo extends JpaRepository<RptBillMain, CompositeKey> {
  public List<RptBillMain> findByBillOffAndBillTelAndBillMonthAndBillId(
      String billOff, String billTel, String billMonth, String billId);

  public List<RptBillMain> findByBillOffAndBillTel(String billOff, String billTel);

  public List<RptBillMain> findByBillTela(String billTela);

  public List<RptBillMain> findByBillIdno(String idno);

  @Query(name = "FindbyBillOpidWithCname", nativeQuery = true)
  public List<Rqbp019Type3Dto> findbyBillOpidWithCname(@Param("billOpid") String billOpid);

  @Query(name = "FindbyBillOpidAndRegexpLikeBillTelaWithCname", nativeQuery = true)
  public List<Rqbp019Type3Dto> findbyBillOpidAndRegexpLikeBillTelaWithCname(
      @Param("billOpid") String billOpid, @Param("pattern") String pattern);
}
