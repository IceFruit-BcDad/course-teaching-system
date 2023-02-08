package com.icefruit.courseteachingsystem.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ResultCode;
import com.icefruit.courseteachingsystem.dto.ChapterDto;
import com.icefruit.courseteachingsystem.error.ServiceException;
import com.icefruit.courseteachingsystem.model.Chapter;
import com.icefruit.courseteachingsystem.repository.ChapterRepository;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChapterService {

    static ILogger logger = SLoggerFactory.getLogger(ChapterService.class);

    private final ChapterRepository chapterRepository;

    private final FileService fileService;

    private final ServiceHelper serviceHelper;

    private final ModelMapper modelMapper;

    public ChapterDto get(long id){
        final Chapter chapter = chapterRepository.findById(id);
        if (chapter == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到此id的章节。");
        }
        return convertToDto(chapter);
    }

    public DtoList<ChapterDto> list(long courseId){
        List<ChapterDto> list = new ArrayList<>();
        Map<Long, ChapterDto> usedMap = new HashMap<>();
        List<Chapter> chapterList = chapterRepository.findAllByCourseId(courseId);
        for (Chapter chapter:
             chapterList) {
            if (usedMap.containsKey(chapter.getId())){
                continue;
            }
            final ChapterDto chapterDto = convertToDto(chapter);
            if (chapterDto.getParentId() == null){
                usedMap.put(chapterDto.getId(), chapterDto);
                list.add(chapterDto);
                continue;
            }
            if (usedMap.containsKey(chapterDto.getParentId())){
                final ChapterDto parentChapter = usedMap.get(chapter.getParentId());
                if (parentChapter.getChildren() == null){
                    parentChapter.setChildren(new ArrayList<>());
                }
                parentChapter.getChildren().add(chapterDto);
                usedMap.put(chapterDto.getId(), chapterDto);
            } else {
                usedMap.put(chapterDto.getId(), chapterDto);
                final Optional<Chapter> parentChapterOptional = chapterList.stream()
                        .filter(item -> item.getId() == chapterDto.getParentId()).findFirst();
                if (parentChapterOptional.isPresent()){
                    final ChapterDto parentChapter = convertToDto(parentChapterOptional.get());
                    if (parentChapter.getChildren() == null){
                        parentChapter.setChildren(new ArrayList<>());
                    }
                    parentChapter.getChildren().add(chapterDto);
                } else {
                    list.add(chapterDto);
                }
            }
        }
        return DtoList.<ChapterDto>builder()
                .total(list.size())
                .list(list)
                .build();
    }

    public ChapterDto create(long courseId, Long parentId, String title, String contentUrl){
        final Instant now = Instant.now();
        Chapter chapter = Chapter.builder()
                .courseId(courseId)
                .parentId(parentId)
                .title(title)
                .contentUrl(contentUrl)
                .createTime(now)
                .lastModifyTime(now)
                .build();
        try {
            chapter = chapterRepository.save(chapter);
            fileService.useFile(contentUrl, Chapter.class, chapter.getId());
        } catch (Exception ex){
            String errMsg = "创建章节失败";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(chapter);
    }

    public ChapterDto update(long id, Long parentId, String title, String contentUrl){
        Chapter chapter = chapterRepository.findById(id);
        if (chapter == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到此id的章节。");
        }
        chapter.setParentId(parentId);
        chapter.setTitle(title);
        chapter.setContentUrl(contentUrl);

        try {
            chapter = chapterRepository.save(chapter);
            if (StringUtils.hasText(contentUrl)){
                fileService.useFile(contentUrl, Chapter.class, chapter.getId());
            }
        } catch (Exception ex){
            String errMsg = "更新章节失败";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(chapter);
    }

    public void delete(long id){
        Chapter chapter = chapterRepository.findById(id);
        if (chapter == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到此id的章节。");
        }
        chapterRepository.deleteById(id);
        fileService.cancelUseFile(chapter.getContentUrl(), Chapter.class, chapter.getId());
    }

    private ChapterDto convertToDto(Chapter chapter){
        return modelMapper.map(chapter, ChapterDto.class);
    }
}
