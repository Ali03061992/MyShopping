package com.tn.shopping.repository.rowmapper;

import com.tn.shopping.domain.FamilleProduct;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FamilleProduct}, with proper type conversions.
 */
@Service
public class FamilleProductRowMapper implements BiFunction<Row, String, FamilleProduct> {

    private final ColumnConverter converter;

    public FamilleProductRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FamilleProduct} stored in the database.
     */
    @Override
    public FamilleProduct apply(Row row, String prefix) {
        FamilleProduct entity = new FamilleProduct();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        return entity;
    }
}
