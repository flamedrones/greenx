package com.flamecode.greenx;

import com.mapbox.geojson.Point;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TrainController {

    @RequestMapping(value = "/train/tickets", method = RequestMethod.GET, produces = "application/json")
    public String trainTickets(@RequestParam(value="startDest") String startDestination,
                        @RequestParam(value="stopDest") String stopDestination){

        Point start = getPoint(startDestination);
        Point stop = getPoint(stopDestination);

        double distance = DistanceCalculator.computeDistance(start, stop);

        TokenRewardInput rewardInput = new TokenRewardInput(distance, 1);
        double reward = TokenRewardCalculator.compute(rewardInput);

        return "{\"distance\": \""+String.format("%.2g%n",distance)+"\", \"reward\": "+String.format("%.2g%n",reward)+"}";
    }

    private Point getPoint(String location){

        String[] splitLocation = location.split(",", 2);

        return  Point.fromLngLat(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));

    }
}
