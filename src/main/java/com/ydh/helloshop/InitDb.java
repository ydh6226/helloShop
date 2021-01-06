package com.ydh.helloshop;

import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void dbInit() {
        initService.init1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final CategoryRepository categoryRepository;

        public void init1() {
            categoryRepository.createRootCategory();
        }
    }
}
