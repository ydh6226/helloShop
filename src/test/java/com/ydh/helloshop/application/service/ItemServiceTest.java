package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.controller.seller.form.ItemForm;
import com.ydh.helloshop.application.domain.Category;
import com.ydh.helloshop.application.domain.item.Album;
import com.ydh.helloshop.application.domain.item.Book;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.item.ItemType;
import com.ydh.helloshop.application.repository.CategoryRepository;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Executable;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @MockBean
    MainService mainService;

    private static final Long SELLER_ID = 1L;

    private static final String IMAGE_URL = "image_path";

    private static final String ITEM_NAME = "원피스";

    @Test
    @DisplayName("상품 등록")
    void registerItem() throws Exception {
        //given
        Long categoryId = createCategory();
        System.out.println(categoryId);
        given(mainService.imageUpload(any())).willReturn(IMAGE_URL);

        //when
        itemService.registerItem(createItemForm(categoryId) ,SELLER_ID);

        //then
        Optional<Item> optionalItem = itemRepository.findByName(ITEM_NAME);
        assertThat(optionalItem).isNotEmpty();

        Item item = optionalItem.get();
        assertThat(item).isInstanceOf(Album.class);
    }

    @Test
    @DisplayName("상품 등록 실패 - 없는 카테고리")
    void registerItemWithInvalidCategory() throws Exception {
        //given
        createCategory();
        given(mainService.imageUpload(any())).willReturn(IMAGE_URL);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> itemService.registerItem(createItemForm(Long.MAX_VALUE), SELLER_ID));

        //then
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 카테고리입니다.");
    }

    private ItemForm createItemForm(Long categoryId) {
        return ItemForm.builder()
                .itemType(ItemType.ALBUM)
                .categoryId(categoryId)
                .name(ITEM_NAME)
                .price(10000)
                .stockQuantity(100)
                .description("재밌습니다.")
                .representativeImageFile(new MockMultipartFile("대표이미지", new byte[]{}))
                .artist("김가수")
                .etc("기타사항").build();
    }

    private Long createCategory() {
        Category category = categoryRepository.save(Category.createCategory("만화책", null));
        return category.getId();
    }
}