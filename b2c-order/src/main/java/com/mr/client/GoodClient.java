package com.mr.client;

import com.mr.service.api.GoodApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodClient extends GoodApi {



}
