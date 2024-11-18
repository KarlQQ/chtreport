package ccbs.service.intf;

import ccbs.dao.core.entity.CommOffice;

import java.util.List;

public interface CommOfficeService {

    public CommOffice get(CommOffice input);

    public List<String> getDistinctBillOffId();

    public CommOffice getAllRelatedOffices(String officeCode, String transType);
}
