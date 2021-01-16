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
import static com.ydh.helloshop.domain.MemberStatus.CUSTOMER;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void dbInit() {
        initService.createRootCategory();
        initService.createAdminMember();
        initService.createCustomerMember();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberRepository memberRepository;

        private final CategoryRepository categoryRepository;

        public void createRootCategory() {
            Category root = createCategory("root", null);
            categoryRepository.save(root);

            //아래는 따로 빼기
            Category c1 = createCategory("의류", root);
            Category c2 = createCategory("도서", root);
            Category c11 = createCategory("상의", c1);
            Category c12 = createCategory("하의", c1);
            Category c21 = createCategory("만화", c2);
            Category c111 = createCategory("반팔", c11);

            categoryRepository.save(c1);
            categoryRepository.save(c2);
            categoryRepository.save(c11);
            categoryRepository.save(c12);
            categoryRepository.save(c21);
            categoryRepository.save(c111);
        }

        public void createAdminMember() {
            Member member = new Member();
            member.createInfo("admin", "admin", new BCryptPasswordEncoder().encode("admin"),
                    null, ADMIN);
            memberRepository.save(member);
        }

        public void createCustomerMember() {
            Member member = new Member();
            member.createInfo("cus", "cus", new BCryptPasswordEncoder().encode("cus"),
                    null, CUSTOMER);
            memberRepository.save(member);
        }

    }
}
