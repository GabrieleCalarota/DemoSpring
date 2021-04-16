package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDAO{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        final String sql = "INSERT INTO Person(id, name) VALUES (?, ?)";
        return jdbcTemplate.update(sql,
                id, person.getName());
    }

    @Override
    public List<Person> selectAllPeople() {
        final String sql = "SELECT id, name from person";
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            final UUID id = UUID.fromString(resultSet.getString("id"));
            final String name = resultSet.getString("name");
            return new Person(id, name);
        }));
    }

    @Override
    public int deletePersonById(UUID id) {
        final String sql = "DELETE FROM person where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Person> updatePersonById(UUID id, Person newPerson) {
        final String sql = "UPDATE person SET name = ? where id = ?";
        jdbcTemplate.update(sql, newPerson.getName(), id);
        return selectPersonById(id);
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        final String sql = "SELECT id, name from person where id = ?";
        Person person = (Person) jdbcTemplate.query(sql,
                new Object[]{id},
                ((resultSet, i) -> {
                                    final UUID personId = UUID.fromString(resultSet.getString("id"));
                                    final String name = resultSet.getString("name");
                                    return new Person(personId, name);
                                }
                                )
                ).get(0);
        return Optional.of(person);
    }
}
