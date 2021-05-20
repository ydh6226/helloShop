package com.ydh.helloshop;

import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.Category;
import com.ydh.helloshop.application.domain.member.CreateMemberParam;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.repository.CategoryRepository;
import com.ydh.helloshop.application.repository.MemberRepository;
import com.ydh.helloshop.application.service.CartService;
import com.ydh.helloshop.application.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static com.ydh.helloshop.application.domain.Category.createCategory;
import static com.ydh.helloshop.application.domain.member.MemberStatus.*;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

//    @PostConstruct
    public void dbInit() {
        initService.createRootCategory();
        initService.createAdminMember();
        initService.createCustomerMember();
        initService.createSellerMember();
//        initService.createItems();
//        initService.createCartItem();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberRepository memberRepository;

        private final CategoryRepository categoryRepository;

        private final ItemService itemService;

        private final CartService cartService;

        private final PasswordEncoder passwordEncoder;

        public void createRootCategory() {
            Category root = createCategory("root", null);
            categoryRepository.save(root);

            Category c1 = createCategory("앨범", root);
            Category c2 = createCategory("도서", root);
            Category c3 = createCategory("가구", root);

            Category c11 = createCategory("유아", c1);
            Category c12 = createCategory("국내", c1);
            Category c13 = createCategory("해외", c1);

            Category c111 = createCategory("영어노래", c11);
            Category c112 = createCategory("한국노래", c11);

            Category c121 = createCategory("발라드", c12);
            Category c122 = createCategory("댄스", c12);
            Category c123 = createCategory("트로트", c12);

            Category c131 = createCategory("빌보드", c13);
            Category c132 = createCategory("spotify", c13);


            Category c21 = createCategory("사회", c2);
            Category c22 = createCategory("만화", c2);
            Category c23 = createCategory("대학", c2);

            Category c211 = createCategory("행정", c21);
            Category c212 = createCategory("법", c21);

            Category c221 = createCategory("코믹스", c22);
            Category c222 = createCategory("원피스", c22);
            Category c223 = createCategory("나루토", c22);

            Category c231 = createCategory("공학", c23);
            Category c232 = createCategory("사범", c23);

            Category c31 = createCategory("의자", c3);
            Category c32 = createCategory("식탁", c3);
            Category c33 = createCategory("선반", c3);

            Category c311 = createCategory("고정형", c31);
            Category c312 = createCategory("이동형", c31);

            Category c321 = createCategory("원형", c32);
            Category c322 = createCategory("사각", c32);
            Category c323 = createCategory("세모", c32);

            Category c331 = createCategory("일단", c33);
            Category c332 = createCategory("이단", c33);

            categoryRepository.save(c1);
            categoryRepository.save(c2);
            categoryRepository.save(c3);
            categoryRepository.save(c11);
            categoryRepository.save(c12);
            categoryRepository.save(c13);
            categoryRepository.save(c111);
            categoryRepository.save(c112);
            categoryRepository.save(c121);
            categoryRepository.save(c122);
            categoryRepository.save(c123);
            categoryRepository.save(c131);
            categoryRepository.save(c132);
            categoryRepository.save(c21);
            categoryRepository.save(c22);
            categoryRepository.save(c23);
            categoryRepository.save(c211);
            categoryRepository.save(c212);
            categoryRepository.save(c221);
            categoryRepository.save(c222);
            categoryRepository.save(c223);
            categoryRepository.save(c231);
            categoryRepository.save(c232);
            categoryRepository.save(c31);
            categoryRepository.save(c32);
            categoryRepository.save(c33);
            categoryRepository.save(c311);
            categoryRepository.save(c312);
            categoryRepository.save(c321);
            categoryRepository.save(c322);
            categoryRepository.save(c323);
            categoryRepository.save(c331);
            categoryRepository.save(c332);
        }

        public void createAdminMember() {
            Member member = Member.createMember(new CreateMemberParam("admin", "admin",
                    passwordEncoder.encode("admin"), ADMIN, null));
            memberRepository.save(member);
        }

        public void createCustomerMember() {
            Member member = Member.createMember(new CreateMemberParam("cus", "cus",
                    passwordEncoder.encode("cus"), CUSTOMER,
                    new Address("경기", "행신로", "143번길")));
            memberRepository.save(member);
        }

        public void createSellerMember() {
            Member member = Member.createMember(new CreateMemberParam("sel", "sel",
                    passwordEncoder.encode("sel"), SELLER, null));
            memberRepository.save(member);
        }

        /*public void createItems() {
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
        }*/

        public void createCartItem() {
            Item item1 = itemService.findOne(1L);
            Item item2 = itemService.findOne(2L);

            cartService.addToCart(2L, item1, 2);
            cartService.addToCart(2L, item2, 2);
        }
    }
}
