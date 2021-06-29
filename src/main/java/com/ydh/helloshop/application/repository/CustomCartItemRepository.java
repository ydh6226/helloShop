package com.ydh.helloshop.application.repository;

import java.util.List;

public interface CustomCartItemRepository {

    void deleteCartItemsByItemIdsAndMemberId(List<Long> itemIds, Long memberId);
}
