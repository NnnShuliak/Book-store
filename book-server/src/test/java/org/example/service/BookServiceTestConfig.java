package org.example.service;

import com.example.grpc.BookServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BookServiceTestConfig {

    @Bean
    public Channel getChannel(){

        return ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

    @Bean
    public BookServiceGrpc.BookServiceBlockingStub getBlockingStub(Channel channel){
        return BookServiceGrpc.newBlockingStub(channel);
    }



}
