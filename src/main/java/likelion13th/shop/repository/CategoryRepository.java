package likelion13th.shop.repository;

import likelion13th.shop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // → Spring Data JPA가 save, findAll, findById, delete 등 자동 구현
    // → 쿼리 작성 없이도 CRUD 처리 가능, 유지보수 용이
}

// 카테고리 CRUD를 위한 JPA Repository 인터페이스
// 기본 메서드는 JpaRepository에서 자동 제공

