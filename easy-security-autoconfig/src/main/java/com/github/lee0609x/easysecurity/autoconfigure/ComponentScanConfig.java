package com.github.lee0609x.easysecurity.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Lee0609x
 * Date:2020/5/23
 */
@ComponentScan({"com.github.lee0609x.easysecurity.filter.*", "com.github.lee0609x.easysecurity.service.*",//core
        "com.github.lee0609x.easysecurity.api.*"})//api
public class ComponentScanConfig {
}
