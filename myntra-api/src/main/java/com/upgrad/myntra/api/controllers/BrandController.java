package com.upgrad.myntra.api.controllers;
import com.upgrad.myntra.service.business.BrandService;
import com.upgrad.myntra.service.business.CategoryService;
import com.upgrad.myntra.service.business.CustomerService;
import com.upgrad.myntra.service.business.ItemService;
import com.upgrad.myntra.service.entity.BrandEntity;
import com.upgrad.myntra.service.entity.CategoryEntity;
import com.upgrad.myntra.service.entity.ItemEntity;
import com.upgrad.myntra.service.exception.BrandNotFoundException;
import com.upgrad.myntra.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.myntra.api.model.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/brand") public class BrandController {

	@Autowired private BrandService brandService;

	@Autowired private ItemService itemService;

	@Autowired private CategoryService categoryService;

	@Autowired private CustomerService customerService;

	@CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/{brand_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<BrandDetailsResponse> getbrandbyid(@PathVariable("brand_id") String BrandId) throws BrandNotFoundException
{
       BrandEntity brandEntity = brandService.brandByUUID(BrandId);
       List<CategoryEntity> brandcategory= brandEntity.getCategories();
       BrandDetailsResponseAddressState brandDetailsResponseAddressState = new BrandDetailsResponseAddressState().stateName(brandEntity.getAddress().getState().getStateName()).id(UUID.fromString(brandEntity.getAddress().getState().getUuid()));
       BrandDetailsResponseAddress brandDetailsResponseAddress = new BrandDetailsResponseAddress().city(brandEntity.getAddress().getCity()).flatBuildingName(brandEntity.getAddress().getFlatBuilNo()).id(UUID.fromString(brandEntity.getAddress().getUuid())).locality(brandEntity.getAddress().getLocality()).pincode(brandEntity.getAddress().getPincode()).state(brandDetailsResponseAddressState);
       BrandDetailsResponse brandDetailsResponse = new BrandDetailsResponse().brandName(brandEntity.getbrandName()).address(brandDetailsResponseAddress).customerRating(BigDecimal.valueOf(brandEntity.getCustomerRating())).id(UUID.fromString(brandEntity.getUuid())).numberCustomersRated(brandEntity.getNumberCustomersRated());
       for(CategoryEntity categoryEntity : brandcategory)
	   {
	   	CategoryList categoryList= new CategoryList().categoryName(categoryEntity.getCategoryName()).id(UUID.fromString(categoryEntity.getUuid()));
	   	List<ItemEntity> itemlist= itemService.getItemsByCategoryAndBrand(BrandId,categoryEntity.getUuid());
	   	for(ItemEntity itemEntity: itemlist)
		{
			ItemList itemList= new ItemList().itemName(itemEntity.getItemName()).price(itemEntity.getPrice()).id(UUID.fromString(itemEntity.getUuid()));
			categoryList.addItemListItem(itemList);
		}
       brandDetailsResponse.addCategoriesItem(categoryList);
	   }
       return new ResponseEntity<BrandDetailsResponse> (brandDetailsResponse,HttpStatus.OK);
}

	@CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "name/{brand_name}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<BrandListResponse> getbrandsbyname(@PathVariable("brand_name") String brandName) throws BrandNotFoundException
{
	List<BrandEntity> brandlistname= brandService.brandsByName(brandName);
	BrandList brandList = new BrandList();
	for(BrandEntity brandEntity : brandlistname)
	{
		BrandDetailsResponseAddressState brandDetailsResponseAddressState = new BrandDetailsResponseAddressState().stateName(brandEntity.getAddress().getState().getStateName()).id(UUID.fromString(brandEntity.getAddress().getState().getUuid()));
		BrandDetailsResponseAddress brandDetailsResponseAddress = new BrandDetailsResponseAddress().city(brandEntity.getAddress().getCity()).flatBuildingName(brandEntity.getAddress().getFlatBuilNo()).id(UUID.fromString(brandEntity.getAddress().getUuid())).locality(brandEntity.getAddress().getLocality()).pincode(brandEntity.getAddress().getPincode()).state(brandDetailsResponseAddressState);
		brandList.brandName(brandEntity.getbrandName()).address(brandDetailsResponseAddress).customerRating(BigDecimal.valueOf(brandEntity.getCustomerRating())).id(UUID.fromString(brandEntity.getUuid())).numberCustomersRated(brandEntity.getNumberCustomersRated());
		List<CategoryEntity> brandlist = brandEntity.getCategories();
		StringBuilder category = new StringBuilder();
		for(CategoryEntity categoryEntity : brandlist)
		{
			category.append(categoryEntity.getCategoryName()).append(",");
		}
		category= new StringBuilder(category.substring(0,category.length()-2));
		brandList.categories(category.toString());
	}
	BrandListResponse brandListResponse = new BrandListResponse().addBrandsItem(brandList);
	return new ResponseEntity<BrandListResponse>(brandListResponse,HttpStatus.OK);
}

	@CrossOrigin
    @RequestMapping(method = RequestMethod.GET,path = "/category/{category_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<BrandListResponse> getbrandbycategory(@PathVariable("category_id") String categoryId) throws CategoryNotFoundException {
	 List<BrandEntity> brand = brandService.brandByCategory(categoryId);
BrandList brandList = new BrandList();
	for(BrandEntity brandEntity : brand)
	{
		BrandDetailsResponseAddressState brandDetailsResponseAddressState = new BrandDetailsResponseAddressState().stateName(brandEntity.getAddress().getState().getStateName()).id(UUID.fromString(brandEntity.getAddress().getState().getUuid()));
		BrandDetailsResponseAddress brandDetailsResponseAddress = new BrandDetailsResponseAddress().city(brandEntity.getAddress().getCity()).flatBuildingName(brandEntity.getAddress().getFlatBuilNo()).id(UUID.fromString(brandEntity.getAddress().getUuid())).locality(brandEntity.getAddress().getLocality()).pincode(brandEntity.getAddress().getPincode()).state(brandDetailsResponseAddressState);
		brandList.brandName(brandEntity.getbrandName()).address(brandDetailsResponseAddress).customerRating(BigDecimal.valueOf(brandEntity.getCustomerRating())).id(UUID.fromString(brandEntity.getUuid())).numberCustomersRated(brandEntity.getNumberCustomersRated());
		List<CategoryEntity> brandlist = brandEntity.getCategories();
		StringBuilder category = new StringBuilder();
		for(CategoryEntity categoryEntity : brandlist)
		{
			category.append(categoryEntity.getCategoryName()).append(",");
		}
		category= new StringBuilder(category.substring(0,category.length()-2));
		brandList.categories(category.toString());
	}
	BrandListResponse brandListResponse = new BrandListResponse().addBrandsItem(brandList);
return new ResponseEntity<BrandListResponse>(brandListResponse,HttpStatus.OK);
}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, path = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<BrandListResponse> getAllBrand() {

		BrandList brandList = new BrandList();
		List<BrandEntity> Brands =  brandService.getallBrand();
		for(BrandEntity brandEntity : Brands)
		{
			BrandDetailsResponseAddressState brandDetailsResponseAddressState = new BrandDetailsResponseAddressState().stateName(brandEntity.getAddress().getState().getStateName()).id(UUID.fromString(brandEntity.getAddress().getState().getUuid()));
			BrandDetailsResponseAddress brandDetailsResponseAddress = new BrandDetailsResponseAddress().city(brandEntity.getAddress().getCity()).flatBuildingName(brandEntity.getAddress().getFlatBuilNo()).id(UUID.fromString(brandEntity.getAddress().getUuid())).locality(brandEntity.getAddress().getLocality()).pincode(brandEntity.getAddress().getPincode()).state(brandDetailsResponseAddressState);

			brandList.address(brandDetailsResponseAddress).brandName(brandEntity.getbrandName()).customerRating(BigDecimal.valueOf(brandEntity.getCustomerRating()));
		}
		BrandListResponse brandListResponse = new BrandListResponse();
		brandListResponse.addBrandsItem(brandList);
		return new ResponseEntity<BrandListResponse>(brandListResponse,HttpStatus.OK);
	}
}
