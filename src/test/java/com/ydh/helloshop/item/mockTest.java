package com.ydh.helloshop.item;

import com.ydh.helloshop.repository.item.AlbumRepository;
import com.ydh.helloshop.service.item.AlbumService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(SpringExtension.class)
class mockTest {

    @InjectMocks
    AlbumService albumService;

    @Mock
    AlbumRepository albumRepository;


    @Test
    @DisplayName("mock test1")
    public void mockT() throws Exception {
        //given
        Album album = new Album("김영한", "", "jpa 정석", 10000, 100);
        given(albumService.save(album)).willReturn(1L);
        given(albumRepository.findAll()).willReturn(asList(album));

        //when
        List<Album> all = albumRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(1);
        System.out.println("hello");
    }

    @Test
    @DisplayName("mock test2")
    @Disabled("Album 엔티티가 setter를 사용하지 않기 때문에 테스트를 할 수 없음.")
    public void mockTT() throws Exception {
        //given
        Album album = new Album("홍길동", "", "hello", 10000, 100);
        doAnswer(new generateId()).when(albumRepository).save(any(Album.class));

        //when
        Long id = albumService.save(album);

        //then
        assertThat(id).isEqualTo(1L);
    }

    static class generateId implements Answer<Void> {

        static Long id = 0L;

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Album argument = (Album) invocation.getArguments()[0];
//            argument.setId(++id);
            return null;
        }
    }



}