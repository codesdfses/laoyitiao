package com.laoyitiao;

import com.laoyitiao.netty.IMServer;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.net.InetSocketAddress;

@EnableGlobalMethodSecurity(prePostEnabled=true)
@SpringBootApplication
@EnableResourceServer
@Slf4j
public class IMApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        application.run(IMApplication.class, args);

        int port = 8888;
        final IMServer endpoint = new IMServer();
        ChannelFuture future = endpoint.start(
                new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                endpoint.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
}
