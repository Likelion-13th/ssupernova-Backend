// JPA 기반으로 Category 테이블에 대한 CRUD 메서드 자동 제공

package likelion13th.shop.repository;

import likelion13th.shop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 별도 쿼리 없이도 save(), findAll(), findById() 등 기본 메서드 사용 가능
}
