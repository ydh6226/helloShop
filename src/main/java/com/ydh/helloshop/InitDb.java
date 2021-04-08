package com.ydh.helloshop;

import com.ydh.helloshop.controller.seller.form.AlbumForm;
import com.ydh.helloshop.domain.Address;
import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.domain.ItemCategory;
import com.ydh.helloshop.domain.member.Member;
import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.CategoryRepository;
import com.ydh.helloshop.repository.MemberRepository;
import com.ydh.helloshop.repository.item.AlbumRepository;
import com.ydh.helloshop.service.CartService;
import com.ydh.helloshop.service.item.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static com.ydh.helloshop.domain.Category.createCategory;
import static com.ydh.helloshop.domain.ItemCategory.createItemCategory;
import static com.ydh.helloshop.domain.member.MemberStatus.*;
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
        initService.createCartItem();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberRepository memberRepository;

        private final CategoryRepository categoryRepository;

        private final AlbumRepository albumRepository;

        private final ItemServiceImpl itemService;

        private final CartService cartService;

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
            Category c112 = createCategory("나시", c11);
            Category c121 = createCategory("반바지", c12);
            Category c211 = createCategory("코믹스", c21);

            categoryRepository.save(c1);
            categoryRepository.save(c2);
            categoryRepository.save(c11);
            categoryRepository.save(c12);
            categoryRepository.save(c21);
            categoryRepository.save(c111);
            categoryRepository.save(c112);
            categoryRepository.save(c121);
            categoryRepository.save(c211);
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
                    new Address("경기", "행신로", "143번길"), CUSTOMER);
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

            AlbumForm form3 = AlbumForm.builder()
                    .sellerId(3L)
                    .artist("천인국")
                    .name("어서와 c++은 처음이지")
                    .price(5000)
                    .stockQuantity(100).build();

            AlbumForm form4 = AlbumForm.builder()
                    .sellerId(3L)
                    .artist("임채윤")
                    .name("오빠의 옷빨")
                    .price(16000)
                    .stockQuantity(1100).build();

            AlbumForm form5 = AlbumForm.builder()
                    .sellerId(3L)
                    .artist("호아킴")
                    .name("99C")
                    .price(20000)
                    .stockQuantity(500).build();

            AlbumForm form6 = AlbumForm.builder()
                    .sellerId(3L)
                    .artist("이일선")
                    .name("톨스토이")
                    .price(20000)
                    .stockQuantity(500).build();

            AlbumForm form7 = AlbumForm.builder()
                    .sellerId(3L)
                    .artist("조영호")
                    .name("객체지향의 사실과 오해")
                    .price(20000)
                    .stockQuantity(500).build();

            Category c1 = categoryRepository.findById(3L).get();
            Category c2 = categoryRepository.findById(4L).get();
            Category c3 = categoryRepository.findById(6L).get();
            Category c4 = categoryRepository.findById(4L).get();

            ItemCategory ic1 = createItemCategory(c1);
            ItemCategory ic2 = createItemCategory(c2);
            ItemCategory ic3 = createItemCategory(c3);
            ItemCategory ic4 = createItemCategory(c4);
            ItemCategory ic5 = createItemCategory(c4);
            ItemCategory ic6 = createItemCategory(c4);
            ItemCategory ic7 = createItemCategory(c4);
            ItemCategory ic8 = createItemCategory(c4);

            Album album1 = createAlbum(form1, Arrays.asList(ic1, ic2));
            Album album2 = createAlbum(form2, Arrays.asList(ic3, ic4));
            Album album3 = createAlbum(form3, Arrays.asList(ic5));
            Album album4 = createAlbum(form4, Arrays.asList(ic6));
            Album album6 = createAlbum(form6, Arrays.asList(ic7));
            Album album7 = createAlbum(form7, Arrays.asList(ic8));

            albumRepository.save(album1);
            albumRepository.save(album2);
            albumRepository.save(album3);
            albumRepository.save(album4);
            albumRepository.save(album6);
            albumRepository.save(album7);
        }

        public void createCartItem() {
            Item item1 = itemService.findOne(1L);
            Item item2 = itemService.findOne(2L);

            cartService.addToCart(2L, item1, 2);
            cartService.addToCart(2L, item2, 2);
        }

    }
}
