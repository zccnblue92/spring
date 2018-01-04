package com.commmon.util;

import com.api.UserApiController;
import io.swagger.annotations.Api;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class Elastaic {

    private TransportClient client;

   public TransportClient  client() {
       Settings settings = Settings.builder().put("cluster.name", "zc").put("client.transport.sniff", true).build();
       try {
           client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
       } catch (UnknownHostException e) {
           e.printStackTrace();
       }
      /* Settings settings = Settings.builder()
               .put("cluster.name", "sojson-application").build();
       client = TransportClient.builder().settings(settings).build();*/
       return client;
    }

}
