package com.ydh.helloshop;

import com.ydh.helloshop.controller.item.AlbumForm;
import com.ydh.helloshop.controller.member.MemberForm;
import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.domain.ItemCategory;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.domain.MemberStatus;
import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.repository.CategoryRepository;
import com.ydh.helloshop.repository.MemberRepository;
import com.ydh.helloshop.repository.item.AlbumRepository;
import com.ydh.helloshop.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;

import java.util.Arrays;

import static com.ydh.helloshop.domain.Category.createCategory;
import static com.ydh.helloshop.domain.ItemCategory.*;
import static com.ydh.helloshop.domain.MemberStatus.*;
import static com.ydh.helloshop.item.Album.createAlbum;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void dbInit() {
        initService.createRootCategory();
        initService.createAdminMember();
        initService.createCustomerMember();
        initService.createSellerMember();
        initService.createItems();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberRepository memberRepository;

        private final CategoryRepository categoryRepository;

        private final AlbumRepository albumRepository;

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

        public void createSellerMember() {
            Member member = new Member();
            member.createInfo("sel", "sel", new BCryptPasswordEncoder().encode("sel"),
                    null, SELLER);
            memberRepository.save(member);
        }

        public void createItems() {
            AlbumForm form1 = AlbumForm.builder()
                    .sellerId(3L)
                    .artist("김영한")
                    .name("jpa정석")
                    .price(10000)
                    .stockQuantity(100).build();

            AlbumForm form2 = AlbumForm.builder()
                    .sellerId(3L)
                    .artist("전예홍")
                    .name("타입스크립트")
                    .price(20000)
                    .stockQuantity(500).build();

            Category c1 = categoryRepository.findById(3L).get();
            Category c2 = categoryRepository.findById(4L).get();
            Category c3 = categoryRepository.findById(6L).get();

            ItemCategory ic1 = createItemCategory(c1);
            ItemCategory ic2 = createItemCategory(c2);
            ItemCategory ic3 = createItemCategory(c3);

            Album album1 = createAlbum(form1, Arrays.asList(ic1, ic2));
            Album album2 = createAlbum(form2, Arrays.asList(ic2, ic3));

            albumRepository.save(album1);
            albumRepository.save(album2);
        }

    }
}
