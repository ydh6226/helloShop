package com.ydh.helloshop.service.item;

import com.ydh.helloshop.controller.item.ItemDto;
import com.ydh.helloshop.exception.noSuchThat.NoSuchItem;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService<Item> {

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Long save(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Transactional
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

    @Transactional
    public List<Item> findAllBySellerId(Long id) {
        return itemRepository.findAllBySellerIdWithItemCategory(id);
    }

    @Transactional
    public void update(Long id, String name, int price, int stockQuantity) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchItem("The Item could not be found."));
        item.changeInfo(name, price, stockQuantity);
    }

    public List<ItemDto> findItemsWithPaging(PageRequest pageRequest) {
        Page<Item> result = itemRepository.findAllWithPaging(pageRequest);

        Page<ItemDto> map = result.map(i -> new ItemDto(i.getId(), i.getName(), i.getPrice()));

        return map.getContent();
    }
}
