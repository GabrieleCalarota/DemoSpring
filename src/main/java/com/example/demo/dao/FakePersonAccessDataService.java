package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("fakeDAO")
public class FakePersonAccessDataService implements PersonDAO {

    private static List<Person> DB = new ArrayList<>();
    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> personMaybe = selectPersonById(id);
        if (!personMaybe.isPresent()){
            return 0;
        }
        DB.remove(selectPersonById(id).get());
        return 1;
    }

    @Override
    public Optional<Person> updatePersonById(UUID id, Person newPerson) {
        return selectPersonById(id)
                .map(p -> {
                    int indexPersonToUpdate = DB.indexOf(p);
                    if (indexPersonToUpdate >= 0) {
                        Person newNewPerson = new Person(id, newPerson.getName());
                        DB.set(indexPersonToUpdate, newNewPerson);
                        return newNewPerson;
                    }
                    return p;
                });
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return DB.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }
}
