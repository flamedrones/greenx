package com.flamecode.greenx;

import com.flamecode.greenx.model.PurchaseTicket;
import com.flamecode.greenx.model.PurchaseTicketResponse;
import com.flamecode.greenx.model.Ticket;
import com.flamecode.greenx.model.Time;
import com.flamecode.greenx.service.WalletService;
import com.google.firebase.auth.FirebaseAuthException;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController()
@RequestMapping("/train")
public class TrainController {

    @Autowired
    WalletService ws;

    @RequestMapping(value = "/tickets", method = RequestMethod.GET, produces = "application/json")
    public Mono<List<Ticket>> trainTickets(@RequestParam(value = "startDest") String startDestination,
                                           @RequestParam(value = "stopDest") String stopDestination) {
        var mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("pk.eyJ1IjoicmFyZXNwb3BhIiwiYSI6ImNrcDlpcWYwbjBrbG4ydXA3cjBwOG8ycWgifQ.DltEZrDyRw4lb-AqTNeeNw");
        var startDest = Mono.<GeocodingResponse>create(sink -> mapboxGeocoding.query(startDestination).build().enqueueCall(new Callback<>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                sink.success(response.body());
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                sink.error(t);
            }
        })).map(res -> res.features().size() >= 1 ? res.features().get(0) : null);

        var stopDest = Mono.<GeocodingResponse>create(sink -> mapboxGeocoding.query(stopDestination).build().enqueueCall(new Callback<>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                sink.success(response.body());
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                sink.error(t);
            }
        })).map(res -> res.features().size() >= 1 ? res.features().get(0) : null);
        return Mono.zip(startDest, stopDest).map(res -> {
            var start = res.getT1().center();
            var stop = res.getT2().center();
            var distance = DistanceCalculator.computeDistance(start, stop);
            TokenRewardInput rewardInput = new TokenRewardInput(distance, 1);
            var reward = TokenRewardCalculator.compute(rewardInput);
            Random generator = new Random();
            var now = LocalDateTime.now().plusDays(1);
            LocalTime time = LocalTime.MIN.plusSeconds(generator.nextLong());
            LocalTime time2 = LocalTime.MIN.plusSeconds(generator.nextLong());
            if (time.compareTo(time2) > 0) {
                var time3 = time;
                time = time2;
                time2 = time3;
            }
            var startTime = new Time(now.getMonthValue(), now.getDayOfMonth(), time.getHour(), time.getMinute());
            var stopTime = new Time(now.getMonthValue(), now.getDayOfMonth(), time2.getHour(), time2.getMinute());
            var ticket = new Ticket();
            ticket.setStartDestination(res.getT1().placeName());
            ticket.setEndDestination(res.getT2().placeName());
            ticket.setDistance(distance);
            ticket.setPrice(distance / 10);
            ticket.setRewardToken(reward);
            ticket.setTimeLeave(startTime);
            ticket.setTimeArrive(stopTime);
            ticket.setPlatform(Integer.toString(generator.nextInt(10)));
            var list = new ArrayList<Ticket>();
            list.add(ticket);
            return list;
        });
    }

    @PostMapping(value = "/purchase", produces = "application/json")
    public Mono<PurchaseTicketResponse> purchaseTicket(@RequestBody PurchaseTicket order) throws FirebaseAuthException {
        return ws.sendTokenByEmail(order.getEmail(), order.getTicket().getRewardToken())
                .map(transactionId -> new PurchaseTicketResponse(
                        order.getTicket().getRewardToken(),
                        transactionId,
                        "https://ropsten.etherscan.io/tx/" + transactionId));
    }

    private Point getPoint(String location) {

        String[] splitLocation = location.split(",", 2);

        return Point.fromLngLat(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1]));

    }
}
