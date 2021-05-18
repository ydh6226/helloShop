package com.ydh.helloshop.application.service.item;

import com.ydh.helloshop.application.controller.item.ItemDto;
import com.ydh.helloshop.application.controller.seller.form.ItemForm;
import com.ydh.helloshop.application.domain.Category;
import com.ydh.helloshop.application.domain.item.*;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchItem;
import com.ydh.helloshop.application.repository.CategoryRepository;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService<Item> {

    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    public Long registerItem(ItemForm itemForm, Long sellerId) {
        Category category = categoryRepository.findById(itemForm.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        return createItem(itemForm, sellerId, ItemCategory.createItemCategory(category));
    }

    @Override
    public Long save(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    public void deleteItemsByIdsQuery(List<Long> ids) {
        itemRepository.deleteItemsByIdIn(ids);

    }

    @Override
    public Item findOne(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NoSuchItem("The Item could not be found."));
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Item> findAllBySellerId(Long id) {
        return itemRepository.findAllBySellerIdWithItemCategory(id);
    }

    public void update(Long id, String name, int price, int stockQuantity) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchItem("The Item could not be found."));
        item.setBasicInfo(name, price, stockQuantity);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> findItemsWithPaging(PageRequest pageRequest) {
        Page<Item> result = itemRepository.findAllWithPaging(pageRequest);

        Page<ItemDto> map = result.map(i -> new ItemDto(i.getId(), i.getName(), i.getPrice()));

        return map.getContent();
    }

    private Long createItem(ItemForm itemForm, Long sellerId, ItemCategory itemCategory) {
        ItemType itemType = itemForm.getItemType();
        Item item;

        switch (itemType) {
            case ALBUM:
                item = new Album(itemForm.getArtist(), itemForm.getEtc());
                break;
            case BOOK:
                item = new Book(itemForm.getAuthor(), itemForm.getIsbn());
                break;
            case FURNITURE:
                item = new Furniture(itemForm.getLength(), itemForm.getWidth(), itemForm.getHeight());
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 상품종류 입니다.");
        }

        item.setBasicInfo(createItemParam(itemForm, sellerId, itemCategory));
        itemRepository.save(item);
        return itemRepository.save(item).getId();
    }

    private ItemParam createItemParam(ItemForm itemForm, Long sellerId, ItemCategory itemCategory) {
        ItemParam itemParam = new ItemParam();
        itemParam.setName(itemForm.getName());
        itemParam.setPrice(itemForm.getPrice());
        itemParam.setStockQuantity(itemForm.getStockQuantity());
        itemParam.setSellerId(sellerId);
        itemParam.setItemCategory(itemCategory);
        return itemParam;
    }
}
