package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EmployeeManagedRepository {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Find employee IDs of the employees that have ALL the skills requested.
     * This is done through the use of a native query grouping skills and counting instances.
     *
     * The instances of the count should match the number of skills sent to the method.
     *
     * TODO test for more than two.
     *
     * NOTE - Hibernate does not have a way to execute a query to find
     * all employees that have ALL skills.  The only way I could solve this
     * problem was with a native query.
     *
     * @param skillsSet
     * @return
     */
    public List<Long> findEmployeeIdsWithAllSkills(Set<EmployeeSkill> skillsSet) {
        // query to return a list of ids that include all the given skills.
        String selectStmt = "select e.id " +
                "FROM Employee AS e, Employee_Skill AS es " +
                "WHERE e.id = es.id " +
                "AND es.skill in (" +
                    skillsSet
                        .stream()
                        .map((skill) -> { return String.valueOf(skill.ordinal()); })
                        .collect(Collectors.joining(",")) +
                ") GROUP BY e.id HAVING count(es.skill) = " + skillsSet.size();

        // Create the query and set parameters
        Query selectQuery = entityManager.createNativeQuery(selectStmt);

        // execute the query
        List<BigInteger> result = selectQuery.getResultList();

        // Convert native result to Long from BigInteger to match Employee ID type.
        return result.stream()
                .map(BigInteger::longValue)
                .collect(Collectors.toList());

    }
}
