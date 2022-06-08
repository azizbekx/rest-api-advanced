package com.epam.esm.api;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.hatoaes.HateoasAdder;
import com.epam.esm.hatoaes.impl.PaginationHateoasAdderImpl;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.response.SuccessResponse;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code TagApiController} controller api.
 */

@RestController
@RequestMapping("/tags")
public class TagApiController {
    @Autowired
    private TagService tagService;
    @Autowired
    private HateoasAdder<TagDto> hateoasAdder;

    @Autowired
    private PaginationHateoasAdderImpl<TagDto> hateoasAdderForPagination;

    /**
     * Method for getting all object from db
     *
     * @return List<TagDto> entity is found entities in db
     */
    @GetMapping
    public ResponseEntity<PaginationResult<TagDto>> getAll(EntityPage entityPage) {
        PaginationResult<TagDto> paginationResult = tagService.getAll(entityPage);
        //adding pagination hal links
        hateoasAdderForPagination.setResourceName("tags");
        hateoasAdderForPagination.addSelfLinks(paginationResult);
        //adding hateoas for each object
        List<TagDto> tagList = paginationResult.getRecords()
                .stream()
                .peek(hateoasAdder::addSelfLinks)
                .collect(Collectors.toList());
        paginationResult.setRecords(tagList);

        if (entityPage.getSize() == paginationResult.getPage().getTotalRecords()) {
            return new ResponseEntity<>(paginationResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(paginationResult, HttpStatus.PARTIAL_CONTENT);
        }
    }

    /**
     * Method for getting object with id from db
     *
     * @param id it is id of object which is getting
     * @return TagDto entity is found
     */
    @GetMapping("/{id}")
    public TagDto getById(@PathVariable long id) {
        TagDto tagDto = tagService.getById(id);
        hateoasAdder.addFullLinks(tagDto);
        return tagDto;
    }

    @GetMapping("/filter")
    public TagDto getByName(@RequestParam(value = "tag_name") String tag_name) {
        return tagService.getByName(tag_name);
    }

    /**
     * Method for inserting object to db
     *
     * @param tagDto TagDto is object which should get from request
     * @return id of new object which is created in db
     */
    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto insert(@RequestBody TagDto tagDto) {
        return tagService.insert(tagDto);
    }

    /**
     * Method for deleting object with id
     *
     * @param id Id of object is being deleted
     * @return int is id of object
     */
    @DeleteMapping("/delete/{id}")
    public SuccessResponse deleteById(@PathVariable long id) {
        boolean success = tagService.deleteById(id);
        return new SuccessResponse(success, "Object was successfully updated (id = " + id + " )");
    }

    @GetMapping("/top/{userId}")
    public TagDto getTopTagOfUserWithHighestCostOrder(@PathVariable long userId) {
        TagDto tagDto = tagService.getTopUsedOfUser(userId);
        hateoasAdder.addSelfLinks(tagDto);
        return tagDto;
    }
}
