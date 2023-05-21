package com.tn.shopping.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.tn.shopping.domain.ImagePresentation;
import com.tn.shopping.repository.rowmapper.ImagePresentationRowMapper;
import com.tn.shopping.repository.rowmapper.ProductRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the ImagePresentation entity.
 */
@SuppressWarnings("unused")
class ImagePresentationRepositoryInternalImpl
    extends SimpleR2dbcRepository<ImagePresentation, Long>
    implements ImagePresentationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductRowMapper productMapper;
    private final ImagePresentationRowMapper imagepresentationMapper;

    private static final Table entityTable = Table.aliased("image_presentation", EntityManager.ENTITY_ALIAS);
    private static final Table imagesPresentationTable = Table.aliased("product", "imagesPresentation");

    public ImagePresentationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProductRowMapper productMapper,
        ImagePresentationRowMapper imagepresentationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ImagePresentation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.productMapper = productMapper;
        this.imagepresentationMapper = imagepresentationMapper;
    }

    @Override
    public Flux<ImagePresentation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ImagePresentation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ImagePresentationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProductSqlHelper.getColumns(imagesPresentationTable, "imagesPresentation"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(imagesPresentationTable)
            .on(Column.create("images_presentation_id", entityTable))
            .equals(Column.create("id", imagesPresentationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ImagePresentation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ImagePresentation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ImagePresentation> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ImagePresentation process(Row row, RowMetadata metadata) {
        ImagePresentation entity = imagepresentationMapper.apply(row, "e");
        entity.setImagesPresentation(productMapper.apply(row, "imagesPresentation"));
        return entity;
    }

    @Override
    public <S extends ImagePresentation> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
