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

    /**
     * 전체 카테고리 목록을 조회
     * - Category 엔티티 리스트를 CategoryResponse DTO 리스트로 변환하여 반환
     */
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll().stream()  // 1. 모든 Category 엔티티를 스트림 형태로 변환
                .map(CategoryResponse::from)   // 2. 각 Category 객체를 CategoryResponse DTO로 매핑
                .collect(Collectors.toList());  // 3. 결과를 리스트로 수집하여 반환
    }

    /** 개별 카테고리 조회 (특정 ID를 가진 카테고리를 조회) **/
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

    /**
     * 카테고리별 상품 목록 조회
     * - 특정 카테고리에 속한 모든 Item 엔티티를 조회하고,
     *   이를 ItemResponse DTO 리스트로 변환하여 반환
     */
    public List<ItemResponse> getItemsByCategory(Long categoryId) {
        // 1. 해당 카테고리에 속한 모든 상품(Item) 엔티티 조회
        List<Item> items = itemRepository.findAllByCategoryId(categoryId);

        // 2. 각 Item을 ItemResponse DTO로 변환하고 리스트로 수집하여 반환
        return itemRepository.findAllByCategoryId(categoryId).stream()
                .map(ItemResponse::from)    // Item → ItemResponse 매핑
                .collect(Collectors.toList());  // 결과를 List로 수집
    }

}
