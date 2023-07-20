package com.bikkadit.store.repository;

import com.bikkadit.store.entity.Category;
import com.bikkadit.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,String>
{
    //search
    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);

    // other methods
    // custom finder methods
    // query methods
}
