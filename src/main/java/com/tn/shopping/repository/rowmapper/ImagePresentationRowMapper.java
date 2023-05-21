package com.tn.shopping.repository.rowmapper;

import com.tn.shopping.domain.ImagePresentation;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ImagePresentation}, with proper type conversions.
 */
@Service
public class ImagePresentationRowMapper implements BiFunction<Row, String, ImagePresentation> {

    private final ColumnConverter converter;

    public ImagePresentationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ImagePresentation} stored in the database.
     */
    @Override
    public ImagePresentation apply(Row row, String prefix) {
        ImagePresentation entity = new ImagePresentation();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setImageContentType(converter.fromRow(row, prefix + "_image_content_type", String.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", byte[].class));
        entity.setImagesPresentationId(converter.fromRow(row, prefix + "_images_presentation_id", Long.class));
        return entity;
    }
}
