package com.upgrad.myntra.service.business;



import com.upgrad.myntra.service.dao.BrandDao;
import com.upgrad.myntra.service.dao.CategoryDao;
import com.upgrad.myntra.service.entity.BrandEntity;
import com.upgrad.myntra.service.exception.BrandNotFoundException;
import com.upgrad.myntra.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * The method implements the business logic for getting brand details by brand uuid.
     */
    @Override
    public BrandEntity brandByUUID(String brandId) throws BrandNotFoundException {
    if(brandId.isEmpty())
    {
        throw new BrandNotFoundException("RNF-002","Brand id field should not be empty");
    }
    else if(brandDao.brandByUUID(brandId)==null)
    {
        throw new BrandNotFoundException("RNF_001","No brand by this id");
    }
    else
        return brandDao.brandByUUID(brandId);
    }

    /**
     * The method implements the business logic for getting brands by brand name.
     */
    @Override
    public List<BrandEntity> brandsByName(String brandName) throws BrandNotFoundException {
        if (brandName.isEmpty())
        {
            throw new BrandNotFoundException("RNF-003","Brand name field should not be empty");
        }
        else
            return brandDao.brandByName(brandName);
    }

    /**
     * The method implements the business logic for getting all brands ordered by their rating.
     */
    @Override
    public List<BrandEntity> brandsByRating() {
        return brandDao.brandByRating();
    }

    /**
     * The method implements the business logic for getting brands by their category.
     */
    @Override
    public List<BrandEntity> brandByCategory(String categoryId) throws CategoryNotFoundException {
        if(categoryId.isEmpty())
        {
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        else if(brandDao.brandByCategory(categoryId)==null)
        {
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        else
            return brandDao.brandByCategory(categoryId);
    }

    @Override
    public List<BrandEntity> getallBrand() {
        return brandDao.getallBrand();
    }

}
