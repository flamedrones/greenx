package com.flamecode.greenx.controller;

import com.flamecode.greenx.DistanceCalculator;
import com.flamecode.greenx.TokenRewardCalculator;
import com.flamecode.greenx.TokenRewardInput;
import com.flamecode.greenx.model.DBOrder;
import com.flamecode.greenx.model.DroneOrderRequest;
import com.flamecode.greenx.model.RewardRequest;
import com.flamecode.greenx.model.RewardResponse;
import com.flamecode.greenx.repo.OrderRepo;
import com.flamecode.greenx.service.WalletService;
import com.google.firebase.auth.FirebaseAuthException;
import com.mapbox.geojson.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/drone")
public class DroneController {
    @Autowired
    WalletService ws;
    @Autowired
    OrderRepo orders;

    private static final Logger LOGGER = LoggerFactory.getLogger(DroneController.class);

    @PostMapping("/order")
    public Mono<DBOrder> makeOrder(@RequestBody DroneOrderRequest order) {
        LOGGER.info("Making order");
        DBOrder dborder = new DBOrder();
        BeanUtils.copyProperties(order, dborder);
        dborder.setStatus("pending");
        dborder.setOriginal_pos(dborder.getPos());
        return orders.save(dborder);
    }

    @PostMapping("/reward")
    public Mono<RewardResponse> getReward(@RequestBody RewardRequest order) {
        var oorder = orders.findById(order.getOrderId());
        var reward = oorder.map(dborder -> {
            var start = Point.fromLngLat(dborder.getOriginal_pos().getLongitude(), dborder.getOriginal_pos().getLatitude());
            var stop = Point.fromLngLat(dborder.getDelivery_pos().getLongitude(), dborder.getOriginal_pos().getLatitude());
            var distance = DistanceCalculator.computeDistance(start, stop);
            TokenRewardInput rewardInput = new TokenRewardInput(distance, 1);
            return TokenRewardCalculator.compute(rewardInput);
        });
        var transaction = reward.flatMap(rwd -> {
            try {
                return ws.sendTokenByEmail(order.getEmail(), rwd);
            } catch (FirebaseAuthException e) {
                LOGGER.error("Firebase error", e);
            }
            return Mono.just("");
        });
        return Mono.zip(oorder, reward, transaction).map(res -> {
            var response = new RewardResponse();
            response.setEmail(order.getEmail());
            response.setOrderId(res.getT1().getId());
            response.setTransactionId(res.getT3());
            response.setEtherscanUrl("https://ropsten.etherscan.io/tx/" + res.getT3());
            response.setTokenAmount(res.getT2());
            return response;
        });
    }
}
