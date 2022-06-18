package com.jpa.book.jpashop.service;

import com.jpa.book.jpashop.domain.Item.Book;
import com.jpa.book.jpashop.domain.Item.Item;
import com.jpa.book.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        // 영속성 엔티티를 찾아옴
        Item findItem = itemRepository.findOne(itemId);
        findItem.change(name, price, stockQuantity);
        /**
         * 이 이후에 Transaction이 커밋되면서 더티 체킹을 통해서 바뀐 값이 반영된다.
         */
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
