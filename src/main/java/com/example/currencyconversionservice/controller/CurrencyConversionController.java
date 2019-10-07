package com.example.currencyconversionservice.controller;


import com.example.currencyconversionservice.feign.CurrencyExchangeServiceProxy;
import com.example.currencyconversionservice.model.CurrencyConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private Environment environment;

    @Autowired
    private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(path = "currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){

        Map<String,String> params = new HashMap<String,String>();
        params.put("from",from);
        params.put("to",to);

        ResponseEntity<CurrencyConversion> responseEntity=new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",CurrencyConversion.class,params);
        CurrencyConversion response = responseEntity.getBody();
        System.out.println("response" +response.getConversionMultiple()+"  "+response.getId()+" "+quantity);

        CurrencyConversion currencyConversion = new CurrencyConversion(response.getId(),from,to,response.getConversionMultiple(),
                quantity,   quantity.multiply(response.getConversionMultiple()),response.getPort());
    return currencyConversion;
    }


    @GetMapping(path = "currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){

        Map<String,String> params = new HashMap<String,String>();
        params.put("from",from);
        params.put("to",to);


        //ResponseEntity<CurrencyConversion> responseEntity=new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",CurrencyConversion.class,params);
        CurrencyConversion response = currencyExchangeServiceProxy.getCurrencyExchange(from,to);


        CurrencyConversion currencyConversion = new CurrencyConversion(response.getId(),from,to,response.getConversionMultiple(),
                quantity,   quantity.multiply(response.getConversionMultiple()),response.getPort());
        logger.info("{}",currencyConversion);
        return currencyConversion;
    }


}
