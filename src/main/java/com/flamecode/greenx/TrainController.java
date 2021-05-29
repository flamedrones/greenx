package com.flamecode.greenx;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TrainController {

    @RequestMapping(value = "/train/tickets", method = RequestMethod.GET, produces = "application/json")
    public String train(@RequestParam(value="startDest") String startDestination,
                        @RequestParam(value="stopDest") String stopDestination){



        return "{\"result\": \"OK\"}";
    }
}
