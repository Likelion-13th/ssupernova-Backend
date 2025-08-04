// CategoryService.java
// 카테고리 CRUD 및 카테고리별 상품 조회 로직을 담당하는 서비스 클래스

package likelion13th.shop.service;

import likelion13th.shop.DTO.request.CategoryRequest;
import likelion13th.shop.DTO.response.CategoryResponse;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    /** 전체 카테고리 조회 **/
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    /** 개별 카테고리 조회 **/
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        return new CategoryResponse(category.getId(), category.getName());
    }

    /** 카테고리 생성 **/
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        Category saved = categoryRepository.save(category);
        return new CategoryResponse(saved.getId(), saved.getName());
    }

    /** 카테고리 수정 **/
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
        category.setName(request.getName());
        Category updated = categoryRepository.save(category);
        return new CategoryResponse(updated.getId(), updated.getName());
    }

    /** 카테고리 삭제 **/
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /** 카테고리별 상품 목록 조회 **/
    public List<ItemResponse> getItemsByCategory(Long categoryId) {
        List<Item> items = itemRepository.findAllByCategoryId(categoryId);

        return items.stream()
                .map(item -> new ItemResponse(
                        item.getId(),              // item_id
                        item.getItem_name(),       // 상품명 (snake_case 필드 그대로 getter 호출)
                        item.getPrice(),           // 가격
                        item.getImagePath(),       // 이미지 경로
                        item.getBrand(),           // 브랜드
                        item.isNew()               // 신상품 여부
                ))
                .collect(Collectors.toList());
    }

}
