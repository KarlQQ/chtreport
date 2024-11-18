package ccbs.util.comm02;

import ccbs.model.batch.*;

public interface Comm02Service {
//    public RptCryptSingleOut COMM02_0006_single_file_encrypt(RptCryptSingleIn inputStr);
//
//    public RptCryptSingleOut COMM02_0006_single_file_decrypt(RptCryptSingleIn inputStr);

    public RptWatermarkSingleOut COMM02_0007_single_file_watermark(RptWatermarkSingleIn inputStr);

    boolean COMM02_0006_single_file_pdf_encrypt(RptWatermarkSingleIn inputStr);

    RptFileZipEncryptSingleOut COMM02_0006_single_file_zip_encrypt(RptFileZipEncryptSingleIn inputStr);

//    public RptCryptSingleOut COMM02_0006_single_pdf_file_encrypt(RptCryptSingleIn inputStr);
}
