package com.upgrad.myntra.api.controllers;


import com.upgrad.myntra.service.business.CategoryService;
import com.upgrad.myntra.service.entity.CategoryEntity;
import com.upgrad.myntra.service.entity.ItemEntity;
import com.upgrad.myntra.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.myntra.api.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @CrossOrigin
@RequestMapping(method = RequestMethod.GET,path = "/category/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getcategorybyid(@PathVariable ("UUID") String categoryId) throws CategoryNotFoundException {
    CategoryEntity categoryEntity = categoryService.getCategoryById(categoryId);
    List<ItemEntity> items= categoryEntity.getItems();
    CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse().categoryName(categoryEntity.getCategoryName()).id(UUID.fromString(categoryEntity.getUuid()));
for(ItemEntity itementity : items)
    {
        ItemList itemlist= new ItemList().id(UUID.fromString(itementity.getUuid())).itemName(itementity.getItemName()).price(itementity.getPrice());
categoryDetailsResponse.addItemListItem(itemlist);
    }
return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse,HttpStatus.OK);

}
    /**
     * A controller method to get all categories from the database.
     *
     * @return - ResponseEntity<CategoriesListResponse> type object along with Http status OK.
     */
    @CrossOrigin
@RequestMapping(method = RequestMethod.GET,path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getallcategories() {
    List<CategoryEntity> categories = categoryService.getAllCategoriesOrderedByName();
    CategoriesListResponse categoriesListResponse = new CategoriesListResponse();
    for (CategoryEntity categoryEntity : categories) {

     CategoryListResponse categoryListResponse = new CategoryListResponse().id(UUID.fromString(categoryEntity.getUuid())).categoryName(categoryEntity.getCategoryName());
 categoriesListResponse.addCategoriesItem(categoryListResponse);
    }
return new ResponseEntity<CategoriesListResponse>(categoriesListResponse,HttpStatus.OK);
}
}
