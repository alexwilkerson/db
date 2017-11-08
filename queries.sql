-- 1. List a company’s workers by names.

select per_name
from person natural join works natural join job
where comp_id = '8' and end_date is null;

-- 2. List a company’s staff by salary in descending order.

select per_name, pay_rate
from person natural join works natural join job
where comp_id = '8' and pay_type = 'salary'
order by pay_rate desc;

-- 3. List companies’ labor cost (total salaries and wage rates by 1920 hours) 
-- in descending order.

select comp_id, sum(case when pay_type = 'salary'
    then pay_rate  else pay_rate*1920 end) as total_labor_cost

from job natural join works
group by comp_id
order by total_labor_cost desc;

-- 4. Find all the jobs a person is currently holding and worked in the past.

select cate_title, job_code, start_date, end_date
from person natural join works natural join has_category 
natural join job_category
where per_id = 1
order by start_date desc;

-- 5. List a person’s knowledge/skills in a readable format.
---select ks_code,ks_title,ks_description,ks_level ks code needed?

select ks_title,ks_level, ks_description
from has_skill natural join knowledge_skill
where per_id = 1;

-- 6. List the skill gap of a worker between his/her job(s) and his/her skills.

(select ks_code, ks_title
from required_skill natural join works natural join job 
natural join knowledge_skill
where per_id = 1)
minus
(select ks_code, ks_title 
from has_skill natural join knowledge_skill 
where per_id = 1);

-- 7. List the required knowledge/skills of a job/ a job category in a readable
-- format. (two queries)

-- a job
select ks_code, ks_title, ks_level, ks_description
from required_skill natural join knowledge_skill
where job_code = 1;

-- a job category
select ks_code, ks_title, ks_level, ks_description
from skill_set natural join knowledge_skill
where cate_code = 1;

-- 8. List a person’s missing knowledge/skills for a specific job in a readable
-- format.

(select ks_code, ks_title
from required_skill natural join job natural join knowledge_skill
where job_code = 2)
minus
(select ks_code, ks_title 
from has_skill natural join knowledge_skill 
where per_id = 1);

-- 9. List the courses (course id and title) that each alone teaches all the
-- missing knowledge/skills for a person to pursue a specific job.

with 
missing_ks(ks) as
((select ks_code 
from required_skill natural join job
where job_code = 1)
minus
(select ks_code
from has_skill
where per_id = 1))

select c_code, c_title
from course c
where not exists
((select *
from missing_ks)
minus
(select ks_code
from teaches_skill ts
where ts.c_code = c.c_code));

-- 10. Suppose the skill gap of a worker and the requirement of a desired job
-- can be covered by one course. Find the “quickest” solution for this worker.
-- Show the course, section information and the completion date.

with
missing_ks(ks) as
((select ks_code 
from required_skill natural join job
where job_code = 1)
minus
(select ks_code
from has_skill
where per_id = 1)),

fufilling_courses(c_code) as
(select c_code, c_title
from course c
where not exists
((select *
from missing_ks)
minus
(select ks_code
from teaches_skill ts
where ts.c_code = c.c_code))),

fufilling_section(c_code, complete_date) as
(select c_code, complete_date 
    from section s natural join fufilling_courses
    where s.course_id = c.id)

select c_code, complete_date
from fufilling_section
where complete_date = (select min(complete_date)
                        from fufilling_section)
;---not working

-- 11. Find the cheapest course to make up one’s skill gap by showing the
-- course to take and the cost (of the section price).

with
missing_ks(ks) as
((select ks_code 
from required_skill natural join job
where job_code = 1)
minus
(select ks_code
from has_skill
where per_id = 1)),

fufilling_courses(c_code) as
(select c_code, c_title
from course c
where not exists
((select *
from missing_ks)
minus
(select ks_code
from teaches_skill ts
where ts.c_code = c.c_code)))

select c_code, c_title, price
from fufilling_courses
where price = (select min(price) 
                from fufilling_courses natural join section)
;---not working

-- 12. If query #9 returns nothing, then find the course sets that their
-- combination covers all the missing knowledge/ skills for a person to pursue
-- a specific job. The considered course sets will not include more than three
-- courses. If multiple course sets are found, list the course sets (with their
-- course IDs) in the order of the ascending order of the course sets’ total
-- costs.

-- 13. List all the job categories that a person is qualified for.

select cate_code, cate_title
from job_category jc
where not exists
((select ks_code
from skill_set ss
where jc.cate_code = ss.cate_code)
minus
(select ks_code
from has_skill
where per_id = 2));

-- 14. Find the job with the highest pay rate for a person according to his/her
-- skill qualification

with pay_rate_table  as
(select distinct rs.job_code
from required_skill rs
where not exists
((select ks_code
from required_skill rs2
where rs.job_code = rs2.job_code)
minus
(select ks_code 
from has_skill
where per_id = 1)))

select job_code, cate_title,  pay_rate, pay_type
from job natural join pay_rate_table natural join has_category
natural join job_category
where pay_rate = (select max(case when pay_type = 'salary' 
                                    then pay_rate else pay_rate*1920 end)
from job natural join pay_rate_table);

-- 15. List all the names along with the emails of the persons who are
-- qualified for a job.

select per_name, email
from person p
where not exists
((select ks_code
from required_skill
where job_code = 1)
minus
(select ks_code
from has_skill hs
where hs.per_id = p.per_id));

-- 16. When a company cannot find any qualified person for a job, a secondary
-- solution is to find a person who is almost qualified to the job. Make a
-- “missing-one” list that lists people who miss only one skill for a specified
-- job.

select per_id, per_name
from person p
where  1 = (select count(*)
            from ((select ks_code
                    from required_skill
                    where job_code = 1)
            minus
            (select ks_code
            from has_skill hs
            where hs.per_id = p.per_id)));
       
-- 17. List the skillID and the number of people in the missing-one list for a
-- given job code in the ascending order of the people counts.
--- redo

with
required_skills(ks_code) as 
(select ks_code
from required_skill
where job_code = 1)

missing_one(per_id) as 
(select per_id, per_name
from person p
where  1 = (select count(*)
            from ((select ks_code
                    from required_skill
                    where job_code = 1)
            minus
            (select ks_code
            from has_skill hs
            where hs.per_id = p.per_id)
            )
))

select ks_code, count(*) as people_count
from ((select * from (select per_id from person join required_skills)
minus
((select * from has_skill) natural join missing_one)))
group by ks_code
order by people_count; -- not working

-- 18. Suppose there is a new job that has nobody qualified. List the persons
-- who miss the least number of skills and report the “least number”.


-- 19. For a specified job category and a given small number k, make a
-- “missing-k” list that lists the people’s IDs and the number of missing
-- skills for the people who miss only up to k skills in the ascending order of
-- missing skills.

-- 20. Given a job category code and its corresponding missing-k list specified
-- in Question 19. Find every skill that is needed by at least one person in
-- the given missing-k list. List each skillID and the number of people who
-- need it
-- in the descending order of the people counts.

-- 21. In a local or national crisis, we need to find all the people who once
-- held a job of the special job category identifier.

with unemployed(per_id) as
((select per_id from person)
minus
(select per_id from works where end_date < CURRENT_DATE))

select per_id
from unemployed natural join job natural join has_category
where cate_code = 1; 



-- 22. Find all the unemployed people who once held a job of the given job
-- identifier.

with unemployed(per_id) as
((select per_id from person)
minus
(select per_id from works where end_date < CURRENT_DATE))

select per_id
from unemployed natural join works
where  job_code = 8;

-- 23. Find out the biggest employer in terms of number of employees or the
-- total amount of salaries and wages paid to employees.

with 
company_size(comp_id, employee_count) as
(select comp_id, count(*)
from job natural join works
group by comp_id)

select comp_id employee_count
from company_size
where employee_count = ( select max ( employee_count ) from company_size);

-- 24. Find out the job distribution among business sectors; find out the
-- biggest sector in terms of number of employees or the total amount of
-- salaries and wages paid to employees.

with
sector_size(primary_sector, employee_count) as
(select primary_sector, count(*)
from job natural join works natural join company
group by primary_sector)

select primary_sector, employee_count
from sector_size
where employee_count = ( select max ( employee_count ) from sector_size);

-- 25. Find out the ratio between the people whose earnings increase and those
-- whose earning decrease; find the average rate of earning improvement for the
-- workers in a specific business sector.

-- 26. Find the leaf-node job categories that have the most openings due to
-- lack of qualified workers. If there are many opening jobs of a job category
-- but at the same time there are many qualified jobless people. Then training
-- cannot help fill up this type of job. What we want to find is such a job
-- category that has the largest difference between vacancies (the unfilled
    -- jobs of this category) and the number of jobless people who are
-- qualified for the jobs of this category.

-- 27. Find the courses that can help most jobless people find a job by
-- training them toward the jobs of this category that have the most openings
-- due to lack of qualified workers.

-- 28. List all the courses, directly or indirectly required, that a person has
-- to take in order to be qualified for a job of the given category, according
-- to his/her skills possessed and courses taken. (required for graduate
    -- students only)4. Find all the jobs a person is currently holding and
-- worked in the past.
