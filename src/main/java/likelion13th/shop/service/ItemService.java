// ItemService.java
// 상품 생성, 조회, 수정, 삭제 등 핵심 비즈니스 로직 수행

package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.request.ItemRequest;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    // 모든 상품 조회
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 상품 단건 조회
    public ItemResponse getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        return toResponse(item);
    }

    // 상품 생성
    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        Item item = new Item();
        item.setItem_name(request.getItemName());
        item.setPrice(request.getPrice());
        item.setImagePath(request.getImagePath());
        item.setBrand(request.getBrand());
        item.setNew(request.isNew());

        // 카테고리 연관관계 설정
        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        item.setCategories(categories);

        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    // 상품 수정
    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        item.setItem_name(request.getItemName());
        item.setPrice(request.getPrice());
        item.setImagePath(request.getImagePath());
        item.setBrand(request.getBrand());
        item.setNew(request.isNew());

        // 연관 카테고리 재설정
        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        item.setCategories(categories);

        return toResponse(item);
    }

    // 상품 삭제
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // Entity → Response 변환 함수
    private ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getItem_name(),
                item.getPrice(),
                item.getImagePath(),
                item.getBrand(),
                item.isNew()
        );
    }
}
