package likelion13th.shop.repository;

// 데이터베이스 접근 계층
// 주문 데이터 접근

import likelion13th.shop.domain.Order;
import likelion13th.shop.global.constant.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime dateTime);
}