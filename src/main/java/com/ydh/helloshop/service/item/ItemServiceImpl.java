package com.ydh.helloshop.service.item;

import com.ydh.helloshop.exception.NoSuchItem;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
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
    public void delete(Long id) {
        itemRepository.deleteById(id);
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
        return itemRepository.findAllBySellerId(id);
    }

    @Transactional
    public void update(Long id, String name, int price, int stockQuantity) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchItem("The Item could not be found."));
        item.changeInfo(name, price, stockQuantity);
    }
}
