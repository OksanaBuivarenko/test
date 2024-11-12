package com.fintech.database.repository;

import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.function.Supplier;

public interface Specs {

    private static <T, V> Specification<T> isEmptyValue(V value, Supplier<Specification<T>> supplier) {
        if ((value == null) || (value instanceof String str && str.isEmpty())) {
            return (r, q, c) -> null;
        }
        return supplier.get();
    }

    static <T, T2, V> Specification<T> eq(SingularAttribute<T, T2> table, SingularAttribute<T2, V> field, V value) {
        return isEmptyValue(value, () -> (root, query, cb) -> {
            if (value instanceof String str) {
                return cb.equal(cb.lower(root.join(table.getName()).get(field.getName())), str.toLowerCase());
            }
            return cb.equal(root.join(table).get(field), value);
        });
    }

    static <T> Specification<T> like(SingularAttribute<T, String> field, String value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.like(cb.lower(root.get(field)), value));
    }

    static <T, V> Specification<T> lessEq(SingularAttribute<T, V> field, LocalDate value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field.getName()), value));
    }

    static <T, V> Specification<T> greaterEq(SingularAttribute<T, V> field, LocalDate value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field.getName()), value));
    }
}