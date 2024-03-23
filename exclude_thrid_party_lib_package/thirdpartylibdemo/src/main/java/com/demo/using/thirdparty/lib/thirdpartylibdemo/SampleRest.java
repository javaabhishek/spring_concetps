package com.demo.using.thirdparty.lib.thirdpartylibdemo;


import com.main.App;
import com.notmain.AnotherApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRest {

    @Autowired
    private App app;

    @Autowired
    private AnotherApp anotherApp;

    @GetMapping("/package/one")
    public String callPackageMainPackageMethod(){
        return app.fromMethodOneOfPackageOne();
    }

    @GetMapping("/package/two")
    public String callPackageTwoPackageMethod(){
        return anotherApp.fromMethodOneOfPackageTwo();
    }
}
