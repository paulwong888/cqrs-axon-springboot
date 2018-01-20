package org.example.cqrs.account.repository;

import org.example.cqrs.account.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Uses 'Spring Data JPA' and 'Spring Data Rest' to create a Repository.
 * The Repository can be used in the EventProcessor to create, read, update and delete the Products in the database.
 * This interface definition hides the Save and Delete operations from the automatically produced REST endpoint.
 * Rest endpoint uses the pluralised name of the Entity by default (/products).
 */

@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends CrudRepository<Account, String> {

    @Override
    @RestResource(exported = false)//true means the capability will be offered via REST
    <S extends Account> S save(S entity);

    @Override
    @RestResource(exported = false)//false restricts the capability from the REST API
    void delete(String id);

    @Override
    @RestResource(exported = false)
    void delete(Account entity);

}