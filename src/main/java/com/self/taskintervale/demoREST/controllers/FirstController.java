package com.self.taskintervale.demoREST.controllers;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
public class FirstController {

    @GetMapping("/hello")   //http://localhost:8080/hello
    public String getHello(){
        return "Hello!";
    }

    @GetMapping("/withParams")   //http://localhost:8080/withParams?singleParamName=paramValue
    public ResponseEntity<String> withParams(@RequestParam("singleParamName") String value){
        return new ResponseEntity<>(value, HttpStatus.ACCEPTED);
    }

    @GetMapping("/withPathVariable/{id}")   //http://localhost:8080/withPathVariable/25
    public ResponseEntity<String> withPathVariable(@PathVariable("id") long id){
        String idValue = Long.toString(id);
        return new ResponseEntity<>(idValue, HttpStatus.CREATED);
    }

    @PostMapping(value = "/echo")  //отправляем запрос по адресу http://localhost:8080/echo
    public ResponseEntity<String> withHeaders1(HttpServletRequest request){
        String header = request.getHeader("Content-Type"); //получаем значение заголовка "Content-Type"

        if(header.startsWith("application/json")){
            return new ResponseEntity<>("json", HttpStatus.OK);
        }
        if(header.startsWith("application/xml")){
            return new ResponseEntity<>("xml", HttpStatus.OK);
        }
        return new ResponseEntity<>(header, HttpStatus.OK);
    }

    @PutMapping("/put")   //http://localhost:8080/put
    public ResponseEntity<String> put(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /*
    @GetMapping("/cookie")   //http://localhost:8080/cookie
    public ResponseEntity getCookie(@CookieValue(value = "firstCookie", defaultValue = "defaultCookie") int i){
        int value = i + 1;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie","firstCookie=" + value);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }
    */

    @GetMapping("/cookie")   //http://localhost:8080/cookie
    public ResponseEntity<String> getCookie(HttpServletRequest request, HttpServletResponse response){
        String dateRequest = new SimpleDateFormat("HH:mm:ss__yyyy.MM.dd").format(Calendar.getInstance().getTime());

        Cookie[] cookies = request.getCookies();

        Cookie cookie = new Cookie("testCookie",dateRequest);
        cookie.setMaxAge(10); //cookie будет жить 10 секунд
        response.addCookie(cookie);

        if(cookies != null){
            for (Cookie cook:cookies) {
                if(cook.getName().equals("testCookie")){
                    String datePreviousRequest = cook.getValue();
                    return new ResponseEntity<>(datePreviousRequest, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
