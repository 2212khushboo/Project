package com.upgrad.myntra.service.business;



import com.upgrad.myntra.service.dao.ItemDao;
import com.upgrad.myntra.service.entity.ItemEntity;
import com.upgrad.myntra.service.exception.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    /**
     * The method implements the business logic for getting list of items based on brand and category uuid.
     */
    @Override
    public List<ItemEntity> getItemsByCategoryAndBrand(String brandId, String categoryId) throws BrandNotFoundException {
        if (brandId.isEmpty()) {
            throw new BrandNotFoundException("RNF-002", "Brand id field should not be empty");
        } else if (itemDao.getItemsByCategoryAndBrand(brandId, categoryId) == null) {
            throw new BrandNotFoundException("RNF-001", "No brand by this id");
        } else {
            return itemDao.getItemsByCategoryAndBrand(brandId, categoryId);
        }
    }
}
