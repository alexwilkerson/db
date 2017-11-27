# Test Cases

## 
### Test Case 1
### Test Case 2
### Expected Result 1
### Expected Result 2

## 8

List a person’s missing knowledge/skills for a specific job in a readable
format.

### Test Case 1

per_id = 1
job_code = 2

### Test Case 2

per_id = 1
job_code = 5

### Expected Result 1

The job code requires skills 9, 6, 12.

The person has skills 13, 14, 16, 2, 28, 6, 9.

The person is missing skills 12.

The result should be 12.

### Expected Result 2

The job code requires skills 2, 22, 5, 6, 9. 

The person has skills 13, 14, 16, 2, 28, 6, 9.

The person is missing skills 22, 5

The Result should be 22, 5.

## 9

List the courses (course id and title) that each alone teaches all the missing
knowledge/skills for a person to pursue a specific job.

### Test Case 1

per_id = 1
job_code = 2

### Test Case 2

per_id = 1
job_code = 6

### Expected Result 1

The person is missing skills 12.

The only class that can fulfill this is course CSCI4125.

The result should be CSCI4125.

### Expected Result 2

The person is missing skills 10, 22

The class LSAT1001 teaches 10, 22, 3
The class CSCI2467  teaches 10, 22, 97
The class CSCI4401 teaches 10, 11, 2, 22, 24, 3 ,9.

##  10

Need courses that end in the future.

Suppose the skill gap of a worker and the requirement of a desired job can be
covered by one course. Find the “quickest” solution for this worker.  Show the
course, section information and the completion date.

### Test Case 1

per_id = 1
job_code = 2

### Test Case 2

### Expected Result 1

### Expected Result 2


## 11 

Suppose the skill gap of a worker and the requirement of a desired job can be
covered by one course. Find the “quickest” solution for this worker.  Show the
course, section information and the completion date.

### Test Case 1 

per_id = 1
job_code = 2

### Test Cases 2

per_id = 1
job_code = 6

### Expected Result 1

The result should be CSCI4125 since it is the only result.

### Expected Result 2

The result should be LSAT1001 since it is the lowest price out the three.

## 11 

Suppose the skill gap of a worker and the requirement of a desired job can be
covered by one course. Find the “quickest” solution for this worker.  Show the
course, section information and the completion date.

### Test Case 1 

per_id = 1
job_code = 2

### Test Cases 2

per_id = 1
job_code = 6

### Expected Result 1

The result should be CSCI4125 since it is the only result.

### Expected Result 2

The result should be LSAT1001 since it is the lowest price out the three.

