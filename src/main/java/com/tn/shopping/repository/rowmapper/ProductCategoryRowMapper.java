package com.tn.shopping.repository.rowmapper;

import com.tn.shopping.domain.ProductCategory;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProductCategory}, with proper type conversions.
 */
@Service
public class ProductCategoryRowMapper implements BiFunction<Row, String, ProductCategory> {

    private final ColumnConverter converter;

    public ProductCategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProductCategory} stored in the database.
     */
    @Override
    public ProductCategory apply(Row row, String prefix) {
        ProductCategory entity = new ProductCategory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCategoryName(converter.fromRow(row, prefix + "_category_name", String.class));
        return entity;
    }
}
