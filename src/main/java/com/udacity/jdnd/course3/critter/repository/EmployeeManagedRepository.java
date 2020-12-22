package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class EmployeeManagedRepository {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Find employee IDs of the employees that have ALL the skills requested
     * This is done through the use of a native query intersecting
     * select queries for each of the skills requested.
     *
     * NOTE - Hibernate does not have a way to execute a query to find
     * all employees that have ALL skills.  The only way I could solve this
     * problem was with a native query.
     *
     * TODO return the employee objects instead of IDs.
     *
     * @param skillsSet
     * @return
     */

    public List<Long> findEmployeeWithAllSkills(Set<EmployeeSkill> skillsSet) {

        // Maping of the query variable to the ordinal of each member of the Enumerated Type
        Map<String, Integer> skillsIdMap = new HashMap<>();

        // query to return a list of ids for a given skill.
        String skillsQuery = "select e.id from Employee AS e " +
                "INNER JOIN employee_skill AS es ON e.id = es.id " +
                "WHERE es.skill = ";

        // using the above statement and the skills given, construct an INTERSECT statement.
        // only the employees that have all the skills required will show up in the result of each query.
        // TODO is there a query that will only select once instead of multiple selects?  Count projection??
        int i = 0;
        String selectQuery = skillsQuery + " :skills_id_0";
        for (EmployeeSkill skill : skillsSet) {
            // Build the query we need for each additional skill
            String key = "skills_id_" + i;
            skillsIdMap.put(key, skill.ordinal());
            if (i > 0) {
                selectQuery += " INTERSECT " + skillsQuery + " :" + key;
            }
            i++;
        }

        // Create the query and set parameters
        Query intersectQuery = entityManager.createNativeQuery(selectQuery);
        for (Map.Entry<String, Integer> entry : skillsIdMap.entrySet()) {
            intersectQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // execute the query
        List result = intersectQuery.getResultList();

        // Convert native result to Long from BigInteger to match Employee ID type.
        // TODO is a better way to do this? Possible to return the Employees?
        List<Long> returnList = new ArrayList<>();
        result.forEach(id -> returnList.add(Long.valueOf(id.toString())));
        return returnList;
    }

}
