package com.jpa.book.jpashop.service;

import com.jpa.book.jpashop.domain.Item.Item;
import com.jpa.book.jpashop.domain.Item.Movie;
import com.jpa.book.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {
    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;

    @Test
    public void 아이템저장() throws Exception {
        // given
//        Movie movie1 = new Movie("director1", "actor1");


        // when

        // then
    }

    @Test
    void findItems() {
        // given

        // when

        // then
    }

    @Test
    void findOne() {
        // given

        // when

        // then
    }
}