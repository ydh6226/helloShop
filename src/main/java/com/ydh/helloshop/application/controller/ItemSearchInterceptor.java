package com.ydh.helloshop.application.controller;

import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemSearchInterceptor implements HandlerInterceptor {

    private final CategoryService categoryService;

    // TODO: 2021-05-16[양동혁] 필요없는 페이지 제외하기 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !isRedirectView(modelAndView)) {
            modelAndView.addObject("itemSearch", new ItemSearch());
            modelAndView.addObject("leafCategories", categoryService.findLeafCategories());
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("redirect:") ||
                modelAndView.getView() instanceof RedirectView;
    }
}
