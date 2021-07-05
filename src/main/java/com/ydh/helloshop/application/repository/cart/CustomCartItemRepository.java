package com.ydh.helloshop.application.repository.cart;

import java.util.List;

public interface CustomCartItemRepository {

    void deleteCartItemsByItemIdsAndMemberId(List<Long> itemIds, Long memberId);
}
