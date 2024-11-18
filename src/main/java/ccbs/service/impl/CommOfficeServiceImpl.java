package ccbs.service.impl;

import ccbs.dao.core.entity.CommOffice;
import ccbs.dao.core.mapper.CommOfficeMapper;
import ccbs.dao.core.sql.CommOfficeDynamicSqlSupport;
import ccbs.service.intf.CommOfficeService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommOfficeServiceImpl implements CommOfficeService {
    @Autowired
    private CommOfficeMapper commOfficeMapper;

    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CommOffice get(CommOffice input) {
    	
    	try {
    		SelectStatementProvider provider = SqlBuilder//
                    .select(CommOfficeDynamicSqlSupport.commOffice.allColumns())//
                    .from(CommOfficeDynamicSqlSupport.commOffice)//
                    .where()//
                    .and(CommOfficeDynamicSqlSupport.office, SqlBuilder.isEqualToWhenPresent(input.getOffice()))//
                    .and(CommOfficeDynamicSqlSupport.lifeStart, SqlBuilder.isEqualToWhenPresent(input.getLifeStart()))//
                    .and(CommOfficeDynamicSqlSupport.lifeEnd, SqlBuilder.isEqualToWhenPresent(input.getLifeEnd()))//
                    .and(CommOfficeDynamicSqlSupport.partId, SqlBuilder.isEqualToWhenPresent(input.getPartId()))//
                    .and(CommOfficeDynamicSqlSupport.offName, SqlBuilder.isEqualToWhenPresent(input.getOffName()))//
                    .and(CommOfficeDynamicSqlSupport.businessNo, SqlBuilder.isEqualToWhenPresent(input.getBusinessNo()))//
                    .and(CommOfficeDynamicSqlSupport.offZip, SqlBuilder.isEqualToWhenPresent(input.getOffZip()))//
                    .and(CommOfficeDynamicSqlSupport.offAddress, SqlBuilder.isEqualToWhenPresent(input.getOffAddress()))//
                    .and(CommOfficeDynamicSqlSupport.offTelno, SqlBuilder.isEqualToWhenPresent(input.getOffTelno()))//
                    .and(CommOfficeDynamicSqlSupport.offFax, SqlBuilder.isEqualToWhenPresent(input.getOffFax()))//
                    .and(CommOfficeDynamicSqlSupport.offEmail, SqlBuilder.isEqualToWhenPresent(input.getOffEmail()))//
                    .and(CommOfficeDynamicSqlSupport.billTelno, SqlBuilder.isEqualToWhenPresent(input.getBillTelno()))//
                    .and(CommOfficeDynamicSqlSupport.boss, SqlBuilder.isEqualToWhenPresent(input.getBoss()))//
                    .and(CommOfficeDynamicSqlSupport.manager, SqlBuilder.isEqualToWhenPresent(input.getManager()))//
                    .and(CommOfficeDynamicSqlSupport.offCity, SqlBuilder.isEqualToWhenPresent(input.getOffCity()))//
                    .and(CommOfficeDynamicSqlSupport.offSw, SqlBuilder.isEqualToWhenPresent(input.getOffSw()))//
                    .and(CommOfficeDynamicSqlSupport.offBill, SqlBuilder.isEqualToWhenPresent(input.getOffBill()))//
                    .and(CommOfficeDynamicSqlSupport.offProc, SqlBuilder.isEqualToWhenPresent(input.getOffProc()))//
                    .and(CommOfficeDynamicSqlSupport.offBelong, SqlBuilder.isEqualToWhenPresent(input.getOffBelong()))//
                    .and(CommOfficeDynamicSqlSupport.offAdmin, SqlBuilder.isEqualToWhenPresent(input.getOffAdmin()))//
                    .and(CommOfficeDynamicSqlSupport.admin, SqlBuilder.isEqualToWhenPresent(input.getAdmin()))//
                    .and(CommOfficeDynamicSqlSupport.admProc, SqlBuilder.isEqualToWhenPresent(input.getAdmProc()))//
                    .and(CommOfficeDynamicSqlSupport.areano, SqlBuilder.isEqualToWhenPresent(input.getAreano()))//
                    .and(CommOfficeDynamicSqlSupport.servTelno, SqlBuilder.isEqualToWhenPresent(input.getServTelno()))//
                    .and(CommOfficeDynamicSqlSupport.servFax, SqlBuilder.isEqualToWhenPresent(input.getServFax()))//
                    .and(CommOfficeDynamicSqlSupport.mailtelno, SqlBuilder.isEqualToWhenPresent(input.getMailtelno()))//
                    .and(CommOfficeDynamicSqlSupport.mailfax, SqlBuilder.isEqualToWhenPresent(input.getMailfax()))//
                    .and(CommOfficeDynamicSqlSupport.wordha, SqlBuilder.isEqualToWhenPresent(input.getWordha()))//
                    .and(CommOfficeDynamicSqlSupport.wordhc, SqlBuilder.isEqualToWhenPresent(input.getWordhc()))//
                    .and(CommOfficeDynamicSqlSupport.wordhe, SqlBuilder.isEqualToWhenPresent(input.getWordhe()))//
                    .and(CommOfficeDynamicSqlSupport.wordhf, SqlBuilder.isEqualToWhenPresent(input.getWordhf()))//
                    .and(CommOfficeDynamicSqlSupport.wordhi, SqlBuilder.isEqualToWhenPresent(input.getWordhi()))//
                    .and(CommOfficeDynamicSqlSupport.errFax, SqlBuilder.isEqualToWhenPresent(input.getErrFax()))//
                    .build()//
                    .render(RenderingStrategies.MYBATIS3);//
    		Optional<CommOffice> optional= commOfficeMapper.selectOne(provider);
    		// 檢查是否有值
            if (optional.isPresent()) {
                return optional.get();
            } else {
                // 處理查無資料的情況
                return null; // 或者拋出自定義異常
            }
    	}catch(Exception e) {
    		throw e;
    	}

    }

	@Override
	public List<String> getDistinctBillOffId() {
		try {
			SelectStatementProvider provider = SqlBuilder//
					.selectDistinct(CommOfficeDynamicSqlSupport.commOffice.offAdmin)//
					.from(CommOfficeDynamicSqlSupport.commOffice)//
					.orderBy(CommOfficeDynamicSqlSupport.commOffice.offAdmin)
					.build()//
					.render(RenderingStrategies.MYBATIS3);//
			List<CommOffice> optional= commOfficeMapper.selectMany(provider);

			List<String> rptOptions = new ArrayList<>();
			for(int i=0; i<optional.size(); i++){
				if(i==0){
					rptOptions.add(optional.get(i).getOffAdmin().trim());
				}else if(!rptOptions.contains(optional.get(i).getOffAdmin())){
					rptOptions.add(optional.get(i).getOffAdmin().trim());
				}
			}

			return rptOptions;

		}catch(Exception e) {
			return null;
		}
	}

    @Override
    public CommOffice getAllRelatedOffices(String officeCode, String transType) {
        Optional<CommOffice> optional= commOfficeMapper.selectOfficeNameByCodeAndType(officeCode, transType);
        // 檢查是否有值
        if (optional.isPresent()) {
            return optional.get();
        } else {
            // 處理查無資料的情況
            return null; // 或者拋出自定義異常
        }
    }
}
