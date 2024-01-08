package pl.patrykdepka.iteventsapi.appuser.domain;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

class AppUserSpecification {

    private AppUserSpecification() {
    }

    static Specification<AppUser> bySearch(String searchWord) {
        return (root, query, criteriaBuilder) -> {
            Predicate firstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchWord + "%");
            Predicate lastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + searchWord + "%");
            Predicate email = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchWord + "%");
            return criteriaBuilder.or(firstName, lastName, email);
        };
    }

    static Specification<AppUser> bySearch(String searchWord, String searchWord2) {
        return (root, query, criteriaBuilder) -> {
            Predicate firstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchWord + "%");
            Predicate lastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + searchWord2 + "%");
            return criteriaBuilder.and(firstName, lastName);
        };
    }
}
