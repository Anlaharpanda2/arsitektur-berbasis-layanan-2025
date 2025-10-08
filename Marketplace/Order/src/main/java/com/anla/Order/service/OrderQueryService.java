package com.anla.Order.service;

import com.anla.Order.model.Order;
import com.anla.Order.model.OrderReadModel;
import com.anla.Order.repository.mongo.OrderReadRepository;
import com.anla.Order.VO.Pelanggan;
import com.anla.Order.VO.Product;
import com.anla.Order.VO.ResponeTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderReadRepository orderReadRepository;
    private final RestTemplate restTemplate;

    public List<OrderReadModel> getAllOrderMongo() {
        log.info("Fetching all orders from MongoDB (Direct - No Conversion)");
        return orderReadRepository.findAll();
    }

    public List<Order> getAllOrder() {
        log.info("Fetching all orders from MongoDB");
        List<OrderReadModel> readModels = orderReadRepository.findAll();
        return convertToOrderList(readModels);
    }

    public Order getOrderById(Long id) {
        // Karena MongoDB menggunakan String ID, kita perlu mencari berdasarkan orderId atau cara lain
        log.info("Fetching order by ID: {} from MongoDB", id);
        // Untuk sementara, kita akan mencari semua dan filter berdasarkan index
        // Ini bukan solusi optimal, tetapi untuk kompatibilitas dengan controller yang existing
        List<OrderReadModel> allOrders = orderReadRepository.findAll();
        if (id > 0 && id <= allOrders.size()) {
            OrderReadModel readModel = allOrders.get((int) (id - 1));
            return convertToOrder(readModel);
        }
        return null;
    }

    public Order getOrderByOrderId(String orderId) {
        log.info("Fetching order by orderId: {} from MongoDB", orderId);
        return orderReadRepository.findByOrderId(orderId)
                .map(this::convertToOrder)
                .orElse(null);
    }

    public List<ResponeTemplate> getOrderWithProdukById(Long id){
        List<ResponeTemplate> responseList = new ArrayList<>();
        Order order = getOrderById(id);
        if (order == null) {
            return responseList;
        }

        // Komunikasi sinkron dengan service lain
        Product produk = restTemplate.getForObject("http://PRODUK-SERVICE/api/produk/"
                + order.getProductId(), Product.class);

        Pelanggan pelanggan = restTemplate.getForObject("http://PELANGGAN-SERVICE/api/pelanggan/"
                + order.getPelangganId(), Pelanggan.class);

        ResponeTemplate vo = new ResponeTemplate();
        vo.setOrder(order);
        vo.setProduk(produk);
        vo.setPelanggan(pelanggan);
        responseList.add(vo);
        return responseList;
    }

    // Helper methods untuk konversi antara OrderReadModel dan Order
    private List<Order> convertToOrderList(List<OrderReadModel> readModels) {
        long startTime = System.currentTimeMillis();
        List<Order> orders = readModels.stream()
                .map(this::convertToOrder)
                .toList();
        long endTime = System.currentTimeMillis();
        log.debug("Object conversion took {} ms for {} records", (endTime - startTime), readModels.size());
        return orders;
    }

    private Order convertToOrder(OrderReadModel readModel) {
        Order order = new Order();
        // Optimized: Use simple counter instead of hash
        order.setId(readModel.getOrderId().hashCode() & 0x7FFFFFFF); // Faster hash
        order.setOrderId(readModel.getOrderId());
        order.setProductId(readModel.getProductId());
        order.setPelangganId(readModel.getPelangganId());
        order.setJumlah(readModel.getJumlah());
        order.setTanggal(readModel.getTanggal());
        order.setStatus(readModel.getStatus());
        order.setTotal(readModel.getTotal());
        return order;
    }
}
