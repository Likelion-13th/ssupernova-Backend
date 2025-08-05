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

    /**
     * 전체 상품 목록 조회
     * - 모든 Item 엔티티를 조회하고, ItemResponse DTO로 변환하여 반환합
     */
    public List<ItemResponse> getAllItems() {
        // → 모든 상품 조회 후 DTO로 변환
        return itemRepository.findAll().stream()
                .map(ItemResponse::from) // 각 Item → ItemResponse 변환
                .collect(Collectors.toList());
    }

    /**
     * 단일 상품 상세 조회
     * - ID에 해당하는 Item이 없으면 예외 발생
     */
    public ItemResponse getItem(Long id) {
        // → ID로 상품 조회, 없으면 예외 발생
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        return ItemResponse.from(item);
    }

    /**
     * 상품 생성
     * - 클라이언트로부터 받은 ItemRequest를 기반으로 Item 엔티티 생성
     * - 카테고리 ID 리스트를 통해 연관관계 설정
     */
    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        // → 요청값으로 Item 엔티티 생성
        Item item = new Item();
        item.setItem_name(request.getItemName());
        item.setPrice(request.getPrice());
        item.setImagePath(request.getImagePath());
        item.setBrand(request.getBrand());
        item.setNew(request.isNew());

        // 연관된 카테고리 설정 (ManyToMany 관계)
        // → 카테고리 ID 리스트로 연관 관계 설정
        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        item.setCategories(categories);

        // DB에 저장 후 DTO로 반환
        Item saved = itemRepository.save(item);
        return ItemResponse.from(saved);
    }

    /**
     * 상품 수정
     * - 기존 상품을 조회한 후, 클라이언트가 전달한 값으로 갱신
     * - 카테고리 연관관계도 재설정
     */
    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest request) {
        // → 기존 상품 조회
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // → 값 수정
        item.setItem_name(request.getItemName());
        item.setPrice(request.getPrice());
        item.setImagePath(request.getImagePath());
        item.setBrand(request.getBrand());
        item.setNew(request.isNew());

        // 기존 연관 카테고리들을 새로 설정
        List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
        item.setCategories(categories);

        return ItemResponse.from(item);
    }

    // 상품 삭제
    // → ID로 삭제
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // → 요청 DTO를 기반으로 엔티티 생성/수정 및 연관관계 처리
    // → 비즈니스 로직 중심으로 컨트롤러의 복잡도 분리

}

// 상품 관련 비즈니스 로직을 처리하는 서비스 클래스
// 요청 DTO 기반으로 엔티티 생성 및 카테고리 연관관계 설정
