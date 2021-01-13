package com.ydh.helloshop;

import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.domain.MemberStatus;
import com.ydh.helloshop.repository.CategoryRepository;
import com.ydh.helloshop.repository.MemberRepository;
import com.ydh.helloshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;

import static com.ydh.helloshop.domain.Category.createCategory;
import static com.ydh.helloshop.domain.MemberStatus.ADMIN;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void dbInit() {
        initService.createRootCategory();
        initService.createAdminMember();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberRepository memberRepository;

        private final CategoryRepository categoryRepository;

        public void createRootCategory() {
            Category category = createCategory("root", null);
            categoryRepository.save(category);
        }

        public void createAdminMember() {
            Member member = new Member();
            member.createInfo("admin", "admin", new BCryptPasswordEncoder().encode("admin"),
                    null, ADMIN);
            memberRepository.save(member);
        }

    }
}
