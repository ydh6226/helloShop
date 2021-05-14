package com.ydh.helloshop.application.api;

import com.ydh.helloshop.application.domain.Cart;
import com.ydh.helloshop.application.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 일대다의 경우 조회시 '다' 쪽에 맞춰서 db row가 뻥튀기됨.
 * 리포지토리에서 반환타입을 엔티티로 했을 경우 하나의 객체로 만들어서 보내주지만
 * 반환타입을 리스트로 했을 경우 중복되는 객체를 반환한다.
 */

@RestController
@RequiredArgsConstructor
public class CartApiController {

    private final CartRepository cartRepository;

    @GetMapping("/api/oneCart")
    Cart findOneCart(@RequestParam Long memberId) {
        return cartRepository.findOneByMemberIdWithItem(memberId);
    }

    @GetMapping("/api/allCart")
    List<Cart> findAllCart(@RequestParam Long memberId) {
        return cartRepository.findAllByMemberIdWithItem(memberId);
    }


}
