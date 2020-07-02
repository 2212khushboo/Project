package com.upgrad.myntra.service.business;


import com.upgrad.myntra.service.dao.CategoryDao;
import com.upgrad.myntra.service.entity.CategoryEntity;
import com.upgrad.myntra.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryDao categoryDao;

    /**
     * The method implements the business logic for getting category by its id endpoint.
     */
    @Override
    public CategoryEntity getCategoryById(String categoryId) throws CategoryNotFoundException {
      if(categoryId.isEmpty())
      {
          throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
      }
      else if(categoryDao.getCategoryById(categoryId)==null)
      {
          throw new CategoryNotFoundException("CNF-002","No category found by this id");
      }
    else
        return categoryDao.getCategoryById(categoryId);
    }

    /**
     * The method implements the business logic for getting all categories ordered by their name endpoint.
     */
    @Override
    public List<CategoryEntity> getAllCategoriesOrderedByName()  {
        return categoryDao.getAllCategoriesOrderedByName();
    }

    /**
     * The method implements the business logic for getting categories for any particular brand.
     */
    @Override
    public List<CategoryEntity> getCategoriesByBrand(String brandId)  {
        return null;
    }
}
