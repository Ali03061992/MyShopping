package com.tn.shopping.repository.rowmapper;

import com.tn.shopping.domain.ObjectContainingImage;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ObjectContainingImage}, with proper type conversions.
 */
@Service
public class ObjectContainingImageRowMapper implements BiFunction<Row, String, ObjectContainingImage> {

    private final ColumnConverter converter;

    public ObjectContainingImageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ObjectContainingImage} stored in the database.
     */
    @Override
    public ObjectContainingImage apply(Row row, String prefix) {
        ObjectContainingImage entity = new ObjectContainingImage();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameContentType(converter.fromRow(row, prefix + "_name_content_type", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", byte[].class));
        entity.setJsonId(converter.fromRow(row, prefix + "_json_id", Long.class));
        return entity;
    }
}
