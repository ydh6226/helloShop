package com.ydh.helloshop.application.controller.seller;

import com.ydh.helloshop.application.factories.MemberFactory;
import com.ydh.helloshop.application.factories.MemberInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class SellerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberFactory memberFactory;

    private static final String EMAIL = "hello@email.com";

    @BeforeEach
    private void initDb() {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setEmail(EMAIL);
        memberFactory.createMember(memberInfo);
    }

    @Test
    @WithUserDetails(value = EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("상품 등록 폼")
    void registerItemForm() throws Exception {
        mockMvc.perform(get(getFullUrl(SellerController.SETTINGS_REGISTER_URL)))
                .andExpect(status().isOk())
                .andExpect(view().name(SellerController.SETTINGS_REGISTER_VIEW))
                .andExpect(model().attributeExists("itemForm", "categories"));
    }

    private String getFullUrl(String subUrl) {
        return "/seller/" + subUrl;
    }
}