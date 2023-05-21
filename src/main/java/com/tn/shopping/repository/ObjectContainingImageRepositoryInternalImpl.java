package com.tn.shopping.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.tn.shopping.domain.ObjectContainingImage;
import com.tn.shopping.repository.rowmapper.ObjectContainingImageRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ObjectContainingImage entity.
 */
@SuppressWarnings("unused")
class ObjectContainingImageRepositoryInternalImpl
    extends SimpleR2dbcRepository<ObjectContainingImage, Long>
    implements ObjectContainingImageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductRowMapper productMapper;
    private final ObjectContainingImageRowMapper objectcontainingimageMapper;

    private static final Table entityTable = Table.aliased("object_containing_image", EntityManager.ENTITY_ALIAS);
    private static final Table jsonTable = Table.aliased("product", "json");

    public ObjectContainingImageRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProductRowMapper productMapper,
        ObjectContainingImageRowMapper objectcontainingimageMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ObjectContainingImage.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.productMapper = productMapper;
        this.objectcontainingimageMapper = objectcontainingimageMapper;
    }

    @Override
    public Flux<ObjectContainingImage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ObjectContainingImage> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ObjectContainingImageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProductSqlHelper.getColumns(jsonTable, "json"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(jsonTable)
            .on(Column.create("json_id", entityTable))
            .equals(Column.create("id", jsonTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ObjectContainingImage.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ObjectContainingImage> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ObjectContainingImage> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ObjectContainingImage process(Row row, RowMetadata metadata) {
        ObjectContainingImage entity = objectcontainingimageMapper.apply(row, "e");
        entity.setJson(productMapper.apply(row, "json"));
        return entity;
    }

    @Override
    public <S extends ObjectContainingImage> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
