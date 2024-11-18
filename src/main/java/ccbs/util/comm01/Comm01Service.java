package ccbs.util.comm01;

import ccbs.model.online.OfficeInfoQueryIn;
import ccbs.model.online.OfficeInfoQueryOut;
import ccbs.model.online.UserInfoQueryIn;
import ccbs.model.online.UserInfoQueryOut;

public interface Comm01Service {
	
    public UserInfoQueryOut COMM001_0005(UserInfoQueryIn inputStr);

    String authValEncrypt(String inputStr);

    public OfficeInfoQueryOut COMM01_0003(OfficeInfoQueryIn inputStr);
    
    
}
