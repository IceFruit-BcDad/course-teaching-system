package com.icefruit.courseteachingsystem.contoller;

import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ListResponse;
import com.icefruit.courseteachingsystem.api.Response;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.dto.ChapterDto;
import com.icefruit.courseteachingsystem.dto.CreateOrUpdateChapterRequest;
import com.icefruit.courseteachingsystem.service.ChapterService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public ListResponse<ChapterDto> getChapters(@RequestParam long courseId){
        DtoList<ChapterDto> list = chapterService.list(courseId);
        return new ListResponse<>(list);

    }

    @PostMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER
    })
    public DataResponse<ChapterDto> create(@RequestBody @Valid CreateOrUpdateChapterRequest request){
        ChapterDto chapterDto = chapterService.create(request.getCourseId(),
                request.getParentId(), request.getTitle(), request.getContentUrl());
        return new DataResponse<>(chapterDto);
    }

    @GetMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<ChapterDto> get(@PathVariable long id){
        final ChapterDto chapterDto = chapterService.get(id);
        return new DataResponse<>(chapterDto);
    }

    @PutMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER
    })
    public DataResponse<ChapterDto> update(@RequestBody @Valid CreateOrUpdateChapterRequest request){
        final ChapterDto chapterDto = chapterService.update(request.getChapterId(), request.getParentId(),
                request.getTitle(), request.getContentUrl());
        return new DataResponse<>(chapterDto);
    }

    @DeleteMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER
    })
    public Response delete(@PathVariable long id){
        chapterService.delete(id);
        return new Response();
    }
}
