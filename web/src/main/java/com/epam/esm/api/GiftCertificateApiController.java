package com.epam.esm.api;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.entity.creteria.GiftSearchCriteria;
import com.epam.esm.hatoaes.HateoasAdder;
import com.epam.esm.hatoaes.impl.PaginationHateoasAdderImpl;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.response.SuccessResponse;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code GiftCertificateApiController} controller api which operation of all gift certificate system.
 */
@RestController
@RequestMapping("/giftcertificates")
public class GiftCertificateApiController {
    @Autowired
    private GiftCertificateService giftService;
    @Autowired
    private HateoasAdder<GiftCertificateDto> hateoasAdder;
    @Autowired
    private PaginationHateoasAdderImpl<GiftCertificateDto> hateoasAdderForPagination;

    /**
     * Method for getting all object from db
     *
     * @return List<GiftCertificateDto> entity is found entities in db
     */
    @GetMapping
    public ResponseEntity<PaginationResult<GiftCertificateDto>> getAll(EntityPage entityPage) {
        PaginationResult<GiftCertificateDto> paginationResult = giftService.getAll(entityPage);
        //adding pagination hal links
        hateoasAdderForPagination.setResourceName("giftcertificates");
        hateoasAdderForPagination.addSelfLinks(paginationResult);
        //adding hateoas for each object
        List<GiftCertificateDto> giftList = paginationResult.getRecords()
                .stream()
                .peek(hateoasAdder::addSelfLinks)
                .collect(Collectors.toList());
        paginationResult.setRecords(giftList);

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
     * @return GiftCertificateDto entity is found
     */
    @GetMapping("/{id}")
    public GiftCertificateDto getById(@PathVariable long id) {
        GiftCertificateDto giftDto = giftService.getById(id);
        hateoasAdder.addFullLinks(giftDto);
        return giftDto;
    }

    /**
     * Method for inserting object to db
     *
     * @param giftCertificateDto is id of object which is getting
     * @return GiftCertificateDto entity is inserted
     */
    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto insert(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto giftDto = giftService.insert(giftCertificateDto);
        hateoasAdder.addFullLinks(giftDto);
        return giftDto;
    }

    /**
     * Method for updating object
     *
     * @param giftCertificateDto is object which is should updated
     * @return GiftCertificateDto which is updated
     */
    @PatchMapping("/update/{id}")
    public GiftCertificateDto update(@PathVariable long id, @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto giftDto = giftService.update(id, giftCertificateDto);
        hateoasAdder.addFullLinks(giftDto);
        return giftDto;
    }

    /**
     * Method for deleting object with id from db
     *
     * @param id it is id of object which is getting
     * @return id of entity is found
     */
    @DeleteMapping("/delete/{id}")
    public @ResponseBody SuccessResponse deleteById(@PathVariable long id) {
        boolean success = giftService.deleteById(id);
        String message = success ? "Object was successfully deleted" : "Object cannot be deleted";
        return new SuccessResponse(success, message + " (id = " + id + " )");
    }

    /**
     * Method for getting objects with based on some order
     *
     * @param searchCriteria SearchCriteria is collection of parameters
     * @param entityPage     EntityPage is object for inserting sort or page data
     * @return GiftCertificateDTO
     */
    @GetMapping("/filter")
    public ResponseEntity<PaginationResult<GiftCertificateDto>> findWithParams(
            GiftSearchCriteria searchCriteria,
            EntityPage entityPage) {
        PaginationResult<GiftCertificateDto> paginationResult = giftService.getWithFilter(searchCriteria, entityPage);
        //adding pagination hal links
        hateoasAdderForPagination.setResourceName("giftcertificates/filter");
        hateoasAdderForPagination.addSelfLinks(paginationResult);
        //adding hateoas for each object
        List<GiftCertificateDto> giftList = paginationResult.getRecords()
                .stream()
                .peek(hateoasAdder::addSelfLinks)
                .collect(Collectors.toList());
        paginationResult.setRecords(giftList);

        if (entityPage.getSize() == paginationResult.getPage().getTotalRecords()) {
            return new ResponseEntity<>(paginationResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(paginationResult, HttpStatus.PARTIAL_CONTENT);
        }
    }

}
