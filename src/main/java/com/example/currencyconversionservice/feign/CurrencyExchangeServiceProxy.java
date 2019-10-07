package com.example.currencyconversionservice.feign;


import com.example.currencyconversionservice.model.CurrencyConversion;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange-service", url="localhost:8000") //Hard URL, call direct
//@FeignClient(name = "currency-exchange-service")  //call direct
@FeignClient(name = "netflix-zuul-api-gateway-server") //through api gate ways
@RibbonClient(name = "currency-exchange-service")
public interface CurrencyExchangeServiceProxy {


    //@GetMapping(path = "/currency-exchange/from/{from}/to/{to}") //with out zuul api gate ways
    @GetMapping(path = "/currency-exchange-service/currency-exchange/from/{from}/to/{to}") //with api gate ways
    public CurrencyConversion getCurrencyExchange(@PathVariable String from, @PathVariable String to);
}
