package com.flamecode.greenx.controller;

import com.flamecode.greenx.FGXManager;
import com.flamecode.greenx.model.DBOrder;
import com.flamecode.greenx.model.Dimension;
import com.flamecode.greenx.model.DroneOrderRequest;
import com.flamecode.greenx.repo.OrderRepo;
import com.flamecode.greenx.repo.UsersRepo;
import com.flamecode.greenx.service.WalletService;
import com.google.cloud.firestore.GeoPoint;
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
    UsersRepo userRepo;
    @Autowired
    FGXManager fgx;
    @Autowired
    WalletService ws;
    @Autowired
    OrderRepo orders;

    @PostMapping("/order")
    public Mono<DBOrder> makeOrder(@RequestBody DroneOrderRequest order) {
        DBOrder dborder = new DBOrder();
        BeanUtils.copyProperties(order, dborder);
        dborder.setStatus("pending");
        return orders.save(dborder);
    }
}
