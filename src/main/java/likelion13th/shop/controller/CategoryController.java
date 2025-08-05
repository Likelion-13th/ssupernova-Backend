package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.request.CategoryRequest;
import likelion13th.shop.DTO.response.CategoryResponse;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "카테고리", description = "카테고리 관련 API 입니다.")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /** 모든 카테고리 조회 **/
    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "전체 카테고리 목록을 조회합니다.")
    public ApiResponse<?> getAllCategories() {
        // 서비스에서 전체 카테고리 리스트 반환 → 응답 포맷 통일
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ApiResponse.onSuccess(SuccessCode.OK, categories);
    }

    /** 개별 카테고리 조회 **/
    @GetMapping("/{categoryId}")
    @Operation(summary = "카테고리 상세 조회", description = "ID로 특정 카테고리를 조회합니다.")
    public ApiResponse<?> getCategory(@PathVariable Long categoryId) {
        // ID 기반 단일 카테고리 조회
        CategoryResponse category = categoryService.getCategory(categoryId);
        return ApiResponse.onSuccess(SuccessCode.OK, category);
    }

    /** 카테고리 생성 **/
    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    public ApiResponse<?> createCategory(@RequestBody CategoryRequest request) {
        // DTO로 전달받은 이름 기반 새 카테고리 생성
        CategoryResponse created = categoryService.createCategory(request);
        return ApiResponse.onSuccess(SuccessCode.CREATED, created);
    }

    /** 카테고리 수정 **/
    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리의 이름을 수정합니다.")
    public ApiResponse<?> updateCategory(@PathVariable Long categoryId,
                                         @RequestBody CategoryRequest request) {
        CategoryResponse updated = categoryService.updateCategory(categoryId, request);
        return ApiResponse.onSuccess(SuccessCode.OK, updated);
    }

    /** 카테고리 삭제 **/
    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "해당 카테고리를 삭제합니다.")
    public ApiResponse<?> deleteCategory(@PathVariable Long categoryId) {
        // ID 기반 삭제 처리
        categoryService.deleteCategory(categoryId);
        return ApiResponse.onSuccess(SuccessCode.OK, "카테고리가 성공적으로 삭제되었습니다.");
    }

    /** 카테고리별 상품 조회 **/
    @GetMapping("/{categoryId}/items")
    @Operation(summary = "카테고리별 상품 조회", description = "특정 카테고리에 속한 상품 목록을 조회합니다.")
    public ApiResponse<?> getItemsByCategory(@PathVariable Long categoryId) {
        // 서비스에서 해당 카테고리에 속한 상품 목록 반환
        List<ItemResponse> items = categoryService.getItemsByCategory(categoryId);

        if (items.isEmpty()) {
            // 상품이 없을 경우 상태 코드 구분
            return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_EMPTY, Collections.emptyList());
        }

        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);
    }
    // → OrderController 패턴과 응답 구조 통일
    // → Swagger 문서화로 API 테스트 편의성 확보
    // → 응답 일관성을 위해 ApiResponse 및 SuccessCode 사용
}

// 카테고리 CRUD 및 카테고리별 상품 조회 기능을 제공하는 컨트롤러
// 일관된 응답 포맷과 Swagger 문서화를 기반으로 구조 설계

