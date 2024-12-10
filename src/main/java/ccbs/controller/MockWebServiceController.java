package ccbs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("local")
@RestController
public class MockWebServiceController {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/transactions/SC01", produces = "application/xml;charset=UTF-8")
    public ResponseEntity<String> handleRequest(@RequestBody String request) {
        log.debug("Received request: " + request);
        // 這裡可以解析請求並返回一個模擬的響應
        String response = "<EAIRSP>\n" +
            "    <Status>0</Status>\n" +
            "    <Msg>成功</Msg>\n" +
            "    <區分公司>S</區分公司>\n" +
            "    <機構代號>15</機構代號>\n" +
            "    <設備號碼>HD09200003</設備號碼>\n" +
            "    <總筆數>4</總筆數>\n" +
            "    <ListItem>\n" +
            "        <出帳年月>104/09</出帳年月>\n" +
            "        <帳單別>R3</帳單別>\n" +
            "        <出帳證號>27183523</出帳證號>\n" +
            "        <帳寄名稱>台</帳寄名稱>\n" +
            "        <郵遞區號>10419</郵遞區號>\n" +
            "        <帳寄地址>台北市中山區中</帳寄地址>\n" +
            "        <名址類別>2</名址類別>\n" +
            "    </ListItem>\n" +
            "    <ListItem>\n" +
            "        <出帳年月>105/01</出帳年月>\n" +
            "        <帳單別>R2</帳單別>\n" +
            "        <出帳證號>98765432</出帳證號>\n" +
            "        <帳寄名稱>新北</帳寄名稱>\n" +
            "        <郵遞區號>23567</郵遞區號>\n" +
            "        <帳寄地址>新北市板橋區民</帳寄地址>\n" +
            "        <名址類別>1</名址類別>\n" +
            "    </ListItem>\n" +
            "    <ListItem>\n" +
            "        <出帳年月>106/03</出帳年月>\n" +
            "        <帳單別>R1</帳單別>\n" +
            "        <出帳證號>12345678</出帳證號>\n" +
            "        <帳寄名稱>高</帳寄名稱>\n" +
            "        <郵遞區號>80001</郵遞區號>\n" +
            "        <帳寄地址>高雄市前金區中</帳寄地址>\n" +
            "        <名址類別>2</名址類別>\n" +
            "    </ListItem>\n" +
            "    <ListItem>\n" +
            "        <出帳年月>107/07</出帳年月>\n" +
            "        <帳單別>R4</帳單別>\n" +
            "        <出帳證號>34567890</出帳證號>\n" +
            "        <帳寄名稱>桃</帳寄名稱>\n" +
            "        <郵遞區號>33010</郵遞區號>\n" +
            "        <帳寄地址>桃園市中壢區桃</帳寄地址>\n" +
            "        <名址類別>3</名址類別>\n" +
            "    </ListItem>\n" +
            "</EAIRSP>";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}