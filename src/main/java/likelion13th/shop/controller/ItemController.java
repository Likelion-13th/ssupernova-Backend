// 상품 CRUD API를 제공하며, Swagger 문서화 및 통일된 응답 포맷 적용

package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.request.ItemRequest;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품", description = "상품 관련 API 입니다.")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /** 전체 상품 목록 조회 **/
    @GetMapping
    @Operation(summary = "모든 상품 조회", description = "전체 상품 목록을 조회합니다.")
    public ApiResponse<?> getAllItems() {
        List<ItemResponse> items = itemService.getAllItems();
        return ApiResponse.onSuccess(SuccessCode.OK, items);
    }

    /** 상품 상세 조회 **/
    @GetMapping("/{itemId}")
    @Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보를 조회합니다.")
    public ApiResponse<?> getItem(@PathVariable Long itemId) {
        // → ID로 단일 상품 조회 후 DTO로 반환
        ItemResponse item = itemService.getItem(itemId);
        return ApiResponse.onSuccess(SuccessCode.ITEM_GET_SUCCESS, item);
    }

    /** 상품 등록 **/
    @PostMapping
    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    public ApiResponse<?> createItem(@RequestBody ItemRequest request) {
        // → 요청 DTO를 받아 새 상품 생성
        ItemResponse item = itemService.createItem(request);
        return ApiResponse.onSuccess(SuccessCode.CREATED, item);
    }

    /** 상품 수정 **/
    @PutMapping("/{itemId}")
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정합니다.")
    public ApiResponse<?> updateItem(@PathVariable Long itemId, @RequestBody ItemRequest request) {
        // → 상품 ID와 DTO를 받아 정보 업데이트
        ItemResponse item = itemService.updateItem(itemId, request);
        return ApiResponse.onSuccess(SuccessCode.OK, item);
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public ApiResponse<?> deleteItem(@PathVariable Long itemId) {
        // → ID로 상품 삭제 수행
        itemService.deleteItem(itemId);
        return ApiResponse.onSuccess(SuccessCode.OK, "상품이 성공적으로 삭제되었습니다.");
    }
    // → OrderController 패턴과 일관되게 설계
    // → Swagger 문서화로 API 테스트 및 설명 명확화
    // → ApiResponse & SuccessCode로 응답 포맷 통일
}

// 상품 CRUD API를 담당하는 컨트롤러
// Swagger 문서화와 응답 포맷 통일로 일관된 구조 제공
