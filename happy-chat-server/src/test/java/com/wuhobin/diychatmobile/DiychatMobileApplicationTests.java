package com.wuhobin.diychatmobile;

import com.wuhobin.entity.ipaddress.AddressResult;
import com.wuhobin.happychatadmin.HappychatAdminApplication;
import com.wuhobin.rest.ipaddress.IpAddressService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HappychatAdminApplication.class)
@Slf4j
class DiychatMobileApplicationTests {

    @Autowired
    private IpAddressService ipAddressService;

    @Test
    public void getIpAddress(){
        AddressResult ipAddress = ipAddressService.getIpAddress("219.140.34.149");
        System.out.println(ipAddress);
    }

}
