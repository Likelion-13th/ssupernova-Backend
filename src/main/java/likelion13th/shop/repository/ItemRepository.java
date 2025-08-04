package likelion13th.shop.repository;

import likelion13th.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 특정 카테고리에 속한 모든 상품을 조회하는 커스텀 쿼리
    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.id = :categoryId")
    List<Item> findAllByCategoryId(Long categoryId);
}
