/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sentinel.rule.dubboconsumer.remote;

import com.sentinel.rule.dubbointeface.DemoService;
import com.sentinel.rule.dubbointeface.dto.UserRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TaskRule implements CommandLineRunner {

    /**
     * 指定具体的server
     */
    @DubboReference(group = "dubbo-one")
    private DemoService demoService;

    @Override
    public void run(String... args) throws Exception {
        UserRequest request = new UserRequest();
        request.setName("world");
        request.setAge(100);
        String result = demoService.sayHello(request);
        System.out.println("Receive result ======> " + result);

        final int[] i = {0};

        new Thread(() -> {
                while (true) {
                    try {
                        if (i[0] == 5){
                            break;
                        }
                        Thread.sleep(1000);
                        request.setName("world-1");
                        request.setAge(101);
                        System.out.println(new Date() + " Receive result ======> " + demoService.sayHello(request));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                   i[0]++;
                }
            }).start();

            i[0]++;

    }
}