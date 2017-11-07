------------------------------------------
-- CLEAR ALL TABLES
------------------------------------------
drop table company_naics;
drop table naics;
drop table preferred_skill;
drop table required_skill;
drop table skill_set;
drop table knowledge_skill;
drop table teaches_skill;
drop table has_skill;
drop table has_category;
drop table has_prereq;
drop table issued_by;
drop table grants_cert;
drop table has_cert;
drop table required_cert;
drop table takes;
drop table section;
drop table course;
drop table certificate;
drop table has_parent;
drop table job_category;
drop table works;
drop table job;
drop table company;
drop table phone;
drop table person;

--------------------------------------
-- RECREATE THE TABLES
--------------------------------------
create table person(
    per_id numeric(8,0),
    per_name varchar(100) not null,
    apt_number varchar(100),
    street_number numeric(20,0),
    street_name varchar(100),
    city varchar(100),
    state varchar(50),
    zip_code numeric(5,0),
    email varchar(100),
    gender varchar(6) check(gender = 'male' or gender = 'female'),
    primary key(per_id)
);

create table phone(
    per_id numeric(8,0),
    phone varchar(20),
    p_type varchar(5) check(p_type = 'cell' or p_type = 'home' or p_type = 'work' or p_type = 'other'),
    primary key(per_id, phone),
    foreign key(per_id) references person on delete set null
);

create table company(
    comp_id varchar(8),
    name varchar(100),
    street_number numeric(10,0),
    street_name varchar(100),
    city varchar(100),
    state varchar(50),
    zip_code numeric(5,0),
    primary_sector varchar(100),
    website varchar(100),
    primary key(comp_id)
);

create table job(
    job_code varchar(8),
    comp_id varchar(8),
    emp_mode varchar(9) check(emp_mode = 'full-time' or emp_mode = 'part-time'),
    pay_rate numeric(8,2) check(pay_rate > 0),
    pay_type varchar(8) check(pay_type = 'wage' or pay_type = 'salary'),
    primary key(job_code),
    foreign key(comp_id) references company on delete cascade
);

create table works(
    job_code varchar(8),
    per_id numeric(8,0),
    start_date date,
    end_date date,
    primary key(job_code, per_id, start_date),
    foreign key(per_id) references person on delete cascade,
    foreign key(job_code) references job on delete cascade
);

create table naics(
    naics_code numeric(6,0),
    title varchar(100),
    primary key(naics_code)
);

create table company_naics(
    comp_id varchar(8),
    naics_code numeric(6,0),
    primary key(comp_id, naics_code),
    foreign key(naics_code) references naics on delete cascade
);

create table job_category(
    cate_code varchar(8),
    title varchar(100),
    description varchar(255),
    pay_range_high numeric(8,2) check (pay_range_high > 0),
    pay_range_low numeric(8,2) check (pay_range_low > 0),
    parent_cate varchar(8),
    primary key(cate_code)
);

create table has_parent(
    cate_code varchar(8),
    parent_cate varchar(8),
    primary key(cate_code, parent_cate),
    foreign key(cate_code) references job_category on delete cascade
);

create table certificate(
    cert_code varchar(20),
    title varchar(100) not null,
    description varchar(500),
    expire_date date,
    issued_by varchar(20),
    t_code varchar(20),
    primary key(cert_code)
);

create table course(
    c_code varchar(8),
    title varchar(100),
    c_level varchar(8) check(c_level = 'beginner' or c_level = 'medium' or c_level = 'advanced'),
    description varchar(500),
    status varchar(6) check(status = 'expired' or status ='active'),
    retail_price numeric(8,2) check (retail_price > 0),
    primary key(c_code)
);

create table section(
    c_code varchar(8),
    sec_no varchar(8),
    year numeric(4,0) check(year > 1701 and year < 2100),
    complete_date date,
    offered_by varchar(20) check(offered_by = 'university' or offered_by = 'training company'),
    format varchar(16) check(format = 'classroom' or format = 'online-sync' or format = 'online-selfpaced' or format = 'correspondence'),
    primary key(c_code, sec_no, year),
    foreign key(c_code) references course on delete cascade
);

create table takes(
    per_id numeric(8,0),
    c_code varchar(8),
    sec_no varchar(8),
    year numeric(4,0) check(year > 1701 and year < 2100),
    primary key(per_id, sec_no, year),
    foreign key(per_id) references person on delete cascade,
    foreign key(c_code, sec_no, year) references section on delete cascade
);

create table required_cert(
    job_code varchar(8),
    cert_code varchar(20),
    primary key(job_code, cert_code),
    foreign key(job_code) references job on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table has_cert(
    per_id numeric(8,0),
    cert_code varchar(20),
    primary key(per_id, cert_code),
    foreign key(per_id) references person on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table grants_cert(
    c_code varchar(8),
    cert_code varchar(20),
    primary key(c_code, cert_code),
    foreign key(c_code) references course on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table issued_by(
    comp_id varchar(8),
    cert_code varchar(20),
    primary key(comp_id, cert_code),
    foreign key(comp_id) references company on delete cascade,
    foreign key(cert_code) references certificate on delete cascade
);

create table has_prereq(
    c_code varchar(8),
    prereq_code varchar(20),
    primary key(c_code, prereq_code),
    foreign key(c_code) references course on delete cascade
);

create table has_category(
    cate_code varchar(8),
    job_code varchar(8),
    primary key(cate_code, job_code),
    foreign key(cate_code) references job_category on delete cascade,
    foreign key(job_code) references job on delete cascade
);

create table has_skill(
    ks_code varchar(8),
    per_id numeric(8,0),
    primary key(ks_code, per_id),
    foreign key(per_id) references person on delete cascade
);

create table teaches_skill(
    ks_code varchar(8),
    c_code varchar(8),
    primary key(ks_code, c_code),
    foreign key(c_code) references course on delete cascade
);

create table knowledge_skill(
    ks_code varchar(8),
    title varchar(100) not null,
    description varchar(500),
    ks_level varchar(8) check(ks_level = 'beginner' or ks_level = 'medium' or ks_level = 'advanced'),
    primary key(ks_code)
);
    
create table skill_set(
    ks_code varchar(8),
    cate_code varchar(8),
    primary key(ks_code, cate_code),
    foreign key(ks_code) references knowledge_skill on delete cascade,
    foreign key(cate_code) references job_category on delete cascade
);

create table required_skill(
    job_code varchar(8),
    ks_code varchar(8),
    primary key(job_code, ks_code),
    foreign key(job_code) references job on delete cascade,
    foreign key(ks_code) references knowledge_skill on delete cascade
);

create table preferred_skill(
    job_code varchar(8),
    ks_code varchar(8),
    primary key(job_code, ks_code),
    foreign key(job_code) references job on delete cascade,
    foreign key(ks_code) references knowledge_skill on delete cascade
);


--------------------------------------
-- INSERT ALL TABLE DATA
--------------------------------------

-- PERSON --
Insert into PERSON (PER_ID,PER_NAME,APT_NUMBER,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,EMAIL,GENDER) values (1,'Carlin Bremer',null,1803,'Broad Brook Crest','Metairie','LA',70001,'carbre@gmail.com','male');
Insert into PERSON (PER_ID,PER_NAME,APT_NUMBER,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,EMAIL,GENDER) values (2,'Sir Prancelot Brainfire',null,666,'Everend Drive','Kenner','LA',70065,'prace_a_lot@aol.com','male');
Insert into PERSON (PER_ID,PER_NAME,APT_NUMBER,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,EMAIL,GENDER) values (3,'Janak Tucci',null,6734,'Green Rise Grounds','New Orleans','LA',70115,'ja.tucci@gmail.com','female');
Insert into PERSON (PER_ID,PER_NAME,APT_NUMBER,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,EMAIL,GENDER) values (4,'Sylvia Boler',null,1519,'Cinder Concession','New Orleans','LA',70122,'deep_lover69@gmail.com','female');
Insert into PERSON (PER_ID,PER_NAME,APT_NUMBER,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,EMAIL,GENDER) values (5,'Robert Christmas','2B',648,'Fire Circle','Gretna','LA',70053,'wingmanPro@yahoo.net','male');

-- PHONE --
Insert into PHONE (PER_ID,PHONE,P_TYPE) values (1,'504-867-5309','cell');
Insert into PHONE (PER_ID,PHONE,P_TYPE) values (2,'504-902-1069','cell');
Insert into PHONE (PER_ID,PHONE,P_TYPE) values (2,'504-555-1212','home');
Insert into PHONE (PER_ID,PHONE,P_TYPE) values (3,'504-420-6969','cell');
Insert into PHONE (PER_ID,PHONE,P_TYPE) values (4,'504-524-1337','cell');
Insert into PHONE (PER_ID,PHONE,P_TYPE) values (4,'504-911-9111','home');
Insert into PHONE (PER_ID,PHONE,P_TYPE) values (5,'504-222-2222','cell');

-- COMPANY --
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('1','Two Sisters',236,'Weird Way','New Orleans','LA',70112,'oil and mining','twosisters.com');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('2','The Diaper Exchange',246,'Poop Street','New Orleans','LA',70115,'entertainment','poophere.org');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('3','Brandon, Inc.',24,'Brandon Drive','Brandon','LA',70178,'entertainment','brandon.com');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('4','The Bradon Bros.',727,'Brandon Avenue','Brandon','LA',70178,'education','brandons.net');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('5','Panda Expressions',5278,'Silly Banana Circle','Metairie','LA',70001,'entertainment','smellthatish.com');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('6','Dungeons and Magazines',11536,'Dancing Way','New Orleans','LA',70122,'computer hardware','rollmag.io');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('7','Help Save Me Co.',587,'This Place','Kenner','LA',70065,'life services','helpsave.me');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('8','Computer Co.',37,'1001011 Avenue','New Orleans','LA',70118,'computer hardware','computer.co');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('9','Gooble',458,'Broadway','New Orleans','LA',70112,'big data','gooble.com');
Insert into COMPANY (COMP_ID,NAME,STREET_NUMBER,STREET_NAME,CITY,STATE,ZIP_CODE,PRIMARY_SECTOR,WEBSITE) values ('10','Facebonk',62,'Main Street','Gretna','LA',70053,'social network','facebonk.com');

-- JOB --
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('1','2','full-time',80000,'salary');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('2','1','part-time',12,'wage');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('3','10','full-time',40000,'salary');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('4','4','part-time',25,'wage');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('5','8','full-time',100000,'salary');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('6','9','full-time',81000,'salary');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('7','3','full-time',45,'wage');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('8','6','full-time',65000,'salary');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('9','7','full-time',70000,'salary');
Insert into JOB (JOB_CODE,COMP_ID,EMP_MODE,PAY_RATE,PAY_TYPE) values ('10','5','full-time',90000,'salary');

-- WORKS --
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('1',1,to_date('04-JUN-14','DD-MON-RR'),to_date('11-DEC-14','DD-MON-RR'));
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('2',1,to_date('01-JAN-15','DD-MON-RR'),to_date('11-MAY-17','DD-MON-RR'));
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('5',1,to_date('18-MAY-17','DD-MON-RR'),null);
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('8',2,to_date('12-NOV-86','DD-MON-RR'),null);
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('3',3,to_date('17-JUN-04','DD-MON-RR'),to_date('15-JAN-10','DD-MON-RR'));
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('10',3,to_date('23-NOV-11','DD-MON-RR'),to_date('06-NOV-17','DD-MON-RR'));
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('9',4,to_date('19-JUN-03','DD-MON-RR'),to_date('16-SEP-04','DD-MON-RR'));
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('1',4,to_date('19-JAN-05','DD-MON-RR'),null);
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('8',4,to_date('21-JUN-17','DD-MON-RR'),null);
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('5',5,to_date('17-NOV-00','DD-MON-RR'),null);
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('4',5,to_date('19-MAY-10','DD-MON-RR'),null);
Insert into WORKS (JOB_CODE,PER_ID,START_DATE,END_DATE) values ('6',5,to_date('15-FEB-14','DD-MON-RR'),to_date('21-NOV-15','DD-MON-RR'));

-- NAICS --
Insert into NAICS (NAICS_CODE,TITLE) values (511210,'Software Publishers');
Insert into NAICS (NAICS_CODE,TITLE) values (518210,'Data Processing, Hosting, and Related Services');
Insert into NAICS (NAICS_CODE,TITLE) values (541511,'Custom Computer Programming Services');
Insert into NAICS (NAICS_CODE,TITLE) values (541512,'Computer Systems Design Services');
Insert into NAICS (NAICS_CODE,TITLE) values (541513,'Computer Facilities Management Services');
Insert into NAICS (NAICS_CODE,TITLE) values (541519,'Other Computer Related Services');
Insert into NAICS (NAICS_CODE,TITLE) values (541715,'Research and Development in the Physical, Engineering, and Life Sciences');
Insert into NAICS (NAICS_CODE,TITLE) values (611420,'Computer Training');

-- COMPANY_NAICS --
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('1',541519);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('10',518210);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('2',541715);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('3',541715);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('4',541715);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('5',541715);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('6',511210);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('7',611420);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('8',541512);
Insert into COMPANY_NAICS (COMP_ID,NAICS_CODE) values ('9',518210);

-- JOB_CATEGORY --
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('1','Diaper Computer Man','You get to work with poop and computers.',60000,85000,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('2','Sister Computer Worker','Work as a computer sister.',11,12,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('3','Social Media Analyst','Work deeply with many facial bonking algorithms.',40000,50000,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('4','Senior Computer Guy','Become prestigious in your field as a Senior Computer Guy.',25,26,'1');
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('5','Computer Employee','Work well with computers.',65000,150000,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('6','Intense Big Data Analyst','Use massive algorithms to awaken machines.',75000,500000,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('7','Exclusive Data Mover','Mover data from one desk to another desk.',40,50,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('8','Electronic Writer Person','Write about electronic things.',60000,75000,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('9','Computer Savior','Fight for the machines!',62000,80000,null);
Insert into JOB_CATEGORY (CATE_CODE,TITLE,DESCRIPTION,PAY_RANGE_HIGH,PAY_RANGE_LOW,PARENT_CATE) values ('10','Luddite','Get away from computers',85000,95000,'5');

-- HAS_PARENT --
Insert into HAS_PARENT (CATE_CODE,PARENT_CATE) values ('10','5');
Insert into HAS_PARENT (CATE_CODE,PARENT_CATE) values ('4','1');

-- CERTIFICATE --

-- COURSE --
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('MATH1115','College Algebra','beginner','Real numbers and equations, functions, polynomial functions and graphs, exponential and logarithmic functions. A strong component of this course will be applications taken from different areas of concentration.','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI1583','Software Design and Development I','beginner','An introduction to software design and development using an object-oriented approach','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('COM2020','Business Communication I','beginner','Convey technical content to any audience through specific, clear and concise writing','active',1200);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('LSAT1001','Linux System Administration Training I','beginner','Our comprehensive Linux best practices courses are the best way for system administrators 
to get the advanced Linux system administration training and Linux security training they 
need to keep their IT infrastructure secure and operating at optimal levels.','active',500);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('BUSI2020','Business Communication I','beginner','Convey technical content to any audience through specific, clear and concise writing','active',700);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('UDW1000','Front-End Web Developer','beginner','Introduction to Front-End Web Development','active',2400);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI1581','Software Design and Development I Laboratory','beginner','Applications, exercises, and explorations in methodologies, software design, and development','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI2120','Software Design and Development II','medium','A continuation of CSCI 1583 and 1581 with emphasis on algorithmic techniques and the structuring of larger systems.','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI2121','Software Design and Development II Laboratory','medium','Applications, exercises, and explorations in methodologies for software design and development.','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI2125','Data Structures','medium','A continuation of CSCI 2120 and 2121 with emphasis on the design and implementation of structured data objects','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI2450','Machine Structure and Assembly Language Programming','medium','Assembly language programming and a survey of computer organization.','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI2467','Systems Programming Concepts','medium',' Introduction to the concepts and tools used in systems programming. ','active',657);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI3301','Computer Organization','medium','Processor design and performance evaluation.','active',512);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI4101','Analysis of Algorithms','advanced','Precise definition of the concept of an algorithm; techniques for algorithm verification; analyzing algorithm performance; applications to practical algorithms.','active',791);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI4125','Data Models and Database Systems','advanced','Methods, structures, and algorithms used for the organization, representation, and manipulation of large data bases','active',534);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI4350','Distributed Software Engineering','advanced','A study of the concepts, the methodology, the models, and methods that address problems in the development of distributed-software applications with emphasis on distributed-object models and components.','active',627);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('CSCI4401','Principles of Operating Systems I','advanced','An introduction to the organization of various types of operating systems; machine structure and the functions of an operating system; multiprogramming and time-sharing environments.','active',680);
Insert into COURSE (C_CODE,TITLE,C_LEVEL,DESCRIPTION,STATUS,RETAIL_PRICE) values ('N/A','No prereq required.','beginner','No prereq required.','active',1);

-- SECTION --
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('COM2020','1',2015,to_date('12-AUG-15','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('COM2020','2',2015,to_date('12-AUG-15','DD-MON-RR'),'university','online-sync');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('LSAT1001','1',2016,to_date('15-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('LSAT1001','2',2016,to_date('15-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('BUSI2020','1',2016,to_date('15-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('BUSI2020','2',2016,to_date('15-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('UDW1000','1',2015,to_date('10-DEC-15','DD-MON-RR'),'training company','online-selfpaced');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('UDW1000','2',2015,to_date('10-DEC-15','DD-MON-RR'),'training company','online-selfpaced');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2121','1',2014,to_date('10-MAY-14','DD-MON-RR'),'university','online-sync');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2121','2',2014,to_date('10-MAY-14','DD-MON-RR'),'university','online-selfpaced');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2120','1',2015,to_date('10-DEC-15','DD-MON-RR'),'training company','online-selfpaced');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2120','2',2015,to_date('10-DEC-15','DD-MON-RR'),'training company','online-selfpaced');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI4125','1',2016,to_date('15-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI4125','2',2016,to_date('15-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI4101','1',2016,to_date('12-AUG-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI4101','2',2016,to_date('12-AUG-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI1583','1',2015,to_date('12-AUG-15','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI1583','2',2015,to_date('12-AUG-15','DD-MON-RR'),'university','online-sync');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI4350','1',2011,to_date('11-DEC-11','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI4350','2',2011,to_date('11-DEC-11','DD-MON-RR'),'university','online-sync');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('MATH1115','1',2011,to_date('11-MAY-11','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('MATH1115','1',2014,to_date('11-DEC-14','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2450','1',2017,to_date('11-DEC-17','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2450','2',2017,to_date('11-DEC-17','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2125','1',2016,to_date('11-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2125','2',2016,to_date('11-DEC-16','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI4401','1',2014,to_date('11-MAY-14','DD-MON-RR'),'university','online-sync');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI1581','1',2017,to_date('05-MAY-17','DD-MON-RR'),'university','online-sync');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI1581','2',2017,to_date('05-MAY-17','DD-MON-RR'),'university','online-sync');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI2467','1',2014,to_date('05-MAY-14','DD-MON-RR'),'university','classroom');
Insert into SECTION (C_CODE,SEC_NO,YEAR,COMPLETE_DATE,OFFERED_BY,FORMAT) values ('CSCI3301','1',2017,to_date('05-MAY-17','DD-MON-RR'),'training company','classroom');

-- TAKES --
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (1,'CSCI4125','1',2016);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (1,'COM2020','1',2015);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (1,'CSCI4350','1',2011);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (1,'CSCI3301','1',2017);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (1,'MATH1115','1',2014);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (2,'CSCI4125','1',2016);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (2,'UDW1000','2',2015);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (2,'CSCI4101','2',2016);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (3,'BUSI2020','2',2016);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (3,'MATH1115','1',2014);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (3,'CSCI2121','2',2014);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (3,'CSCI2450','2',2017);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (3,'CSCI1583','1',2015);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (4,'COM2020','2',2015);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (4,'CSCI3301','1',2017);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (4,'CSCI2450','2',2017);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (5,'CSCI2450','2',2017);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (5,'CSCI4125','1',2016);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (5,'UDW1000','1',2015);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (5,'CSCI4101','2',2016);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (5,'MATH1115','1',2011);
Insert into TAKES (PER_ID,C_CODE,SEC_NO,YEAR) values (5,'CSCI1583','2',2015);

-- REQUIRED_CERT --
-- HAS_CERT --
-- GRANTS_CERT --
-- ISSUED_BY --

-- HAS_PREREQ --
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('BUSI2020','N/A');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('COM2020','N/A');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI1581','N/A');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI1583','MATH1115');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2120','CSCI1581');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2120','CSCI1583');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2121','N/A');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2125','CSCI2120');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2125','CSCI2121');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2450','CSCI1583');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2467','CSCI2120');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI2467','CSCI2450');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI3301','CSCI2120');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI3301','CSCI2450');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI4101','CSCI2125');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI4125','CSCI2125');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI4350','CSCI2125');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI4401','CSCI2125');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('CSCI4401','CSCI2467');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('LSAT1001','N/A');
Insert into HAS_PREREQ (C_CODE,PREREQ_CODE) values ('UDW1000','N/A');

-- HAS_CATEGORY --
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('1','2');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('10','3');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('2','1');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('3','7');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('4','4');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('5','10');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('6','8');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('7','9');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('8','5');
Insert into HAS_CATEGORY (CATE_CODE,JOB_CODE) values ('9','6');

-- HAS_SKILL --
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('1',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('10',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('10',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('11',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('13',1);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('13',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('14',1);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('14',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('14',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('15',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('16',1);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('16',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('17',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('17',4);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('18',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('2',1);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('2',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('21',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('21',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('21',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('22',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('22',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('24',4);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('24',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('25',4);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('26',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('27',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('27',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('27',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('28',1);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('28',3);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('3',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('30',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('4',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('4',4);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('4',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('5',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('5',4);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('5',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('6',1);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('6',2);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('6',5);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('9',1);
Insert into HAS_SKILL (KS_CODE,PER_ID) values ('9',3);

-- TEACHES_SKILL --
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('10','CSCI2120');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('10','CSCI2467');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('10','CSCI3301');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('10','CSCI4401');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('10','LSAT1001');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('11','CSCI4401');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('12','CSCI4125');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('13','CSCI4125');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('14','CSCI4125');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('15','CSCI4350');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('16','CSCI4125');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('2','CSCI2450');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('2','CSCI4101');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('2','CSCI4401');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('21','BUSI2020');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('21','COM2020');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('22','CSCI1583');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('22','CSCI2467');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('22','CSCI4401');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('22','LSAT1001');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('24','CSCI4401');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('27','BUSI2020');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('27','COM2020');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('28','CSCI4125');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('28','MATH1115');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('3','CSCI4401');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('3','LSAT1001');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('4','CSCI1583');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('4','CSCI2120');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('5','CSCI2120');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('6','CSCI1583');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('6','CSCI2120');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('6','CSCI2125');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('6','CSCI4101');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('8','CSCI2120');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('9','CSCI2120');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('9','CSCI2450');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('9','CSCI2467');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('9','CSCI3301');
Insert into TEACHES_SKILL (KS_CODE,C_CODE) values ('9','CSCI4401');

-- KNOWLEDGE_SKILL --
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('1','Adobe Photoshop',' An image editing software developed and manufactured by Adobe Systems Inc. Photoshop is considered one of the leaders in photo editing software. ','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('2','C++','C++ is a general-purpose object-oriented programming (OOP) language, developed by Bjarne Stroustrup, and is an extension of the C language.','advanced');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('3','BASH','Bash is a Unix shell and command language written by Brian Fox for the GNU Project as a free software replacement for the Bourne shell.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('4','HTML','First developed by Tim Berners-Lee in 1990, HTML is short for HyperText Markup Language. HTML is used to create electronic documents (called pages) that are displayed on the World Wide Web.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('5','Git','Git is a version control system for tracking changes in computer files.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('6','Java','Java is a general purpose, high-level programming language developed by Sun Microsystems.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('7','jQuery','jQuery is a cross-platform JavaScript library designed to simplify the client-side scripting of HTML.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('8','Javascript','Javascript is a high-level, dynamic, weakly typed, prototype-based, multi-paradigm, and interpreted programming language.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('9','C','C is a general-purpose, imperative computer programming language.','advanced');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('10','Linux','Linux is a name which broadly denotes a family of free and open-source software operating system distributions built around the Linux kernel.','advanced');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('11','MacIntosh OS','MacIntosh OS is the current series of Unix-based graphical operating systems developed and marketed by Apple Inc. designed to run on Apple''s Macintosh computers ("Macs").','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('12','JDBC','JDBC is an application programming interface (API) for the programming language Java, which defines how a client may access a database.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('13','Oracle','Oracle is a relational database management system.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('14','MySQL','MySQLis a relational database management system','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('15','Microsoft Word','Microsoft Word is a word processor developed by Microsoft.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('16','MongoDB','MongoDB is a free and open-source cross-platform document-oriented database program.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('17','Swift','Swift is a programming language developed by Apple Inc.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('18','Go','Go a programming language developed by Google.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('19','Haskell','Haskell is a standardized pure functional programming language with non-strict semantics.','advanced');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('20','Wireshark','Wireshark is a free and open source packet analyzer.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('21','YouTube','YouTube is an American video-sharing website.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('22','Unix','Unix is a family of multitasking, multiuser computer operating systems that derive from the original ATT Unix.','advanced');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('23','Python','Python is a programming language.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('24','Microsoft Windows','Microsoft Windows, or simply Windows, is a metafamily of graphical operating systems developed, marketed, and sold by Microsoft.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('25','Mobile Application Design','The development of mobile applications.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('26','Heroku','Heroku is a cloud platform as a service (PaaS) supporting several programming languages that is used as a web application deployment model.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('27','Facebook','Facebook is a social media website.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('28','Database Software','General relational database and software knowledge.','medium');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('29','CSS','Cascading Style Sheets (CSS) is a style sheet language used for describing the presentation of a document written in a markup language.','beginner');
Insert into KNOWLEDGE_SKILL (KS_CODE,TITLE,DESCRIPTION,KS_LEVEL) values ('30','VMWare','VMWare is a company that provides cloud computing and platform virtualization software and services.','beginner');

-- SKILL_SET --
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('1','1');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('1','3');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('1','4');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('10','6');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('10','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('10','9');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('11','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('12','3');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('13','8');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('14','2');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('14','3');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('15','2');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('15','4');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('17','4');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('18','4');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('18','5');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('19','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('19','5');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('19','8');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('2','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('2','2');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('20','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('20','8');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('21','1');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('22','1');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('22','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('22','6');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('22','9');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('23','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('24','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('26','3');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('27','1');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('27','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('29','5');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('29','9');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('3','6');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('3','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('30','1');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('30','4');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('30','8');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('4','3');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('4','5');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('4','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('5','3');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('5','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('6','2');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('6','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('6','8');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('7','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('7','2');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('7','4');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('7','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('7','8');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('8','7');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('9','10');
Insert into SKILL_SET (KS_CODE,CATE_CODE) values ('9','7');

-- REQUIRED_SKILL --
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('1','28');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('1','6');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('10','10');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('10','12');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('10','13');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('10','14');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('10','3');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('2','12');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('2','6');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('2','9');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('3','14');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('3','16');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('3','22');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('4','2');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('5','2');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('5','22');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('5','5');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('5','6');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('5','9');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('6','10');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('6','22');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('7','27');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('7','6');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('7','9');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('8','14');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('8','2');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('8','21');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('8','27');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('9','10');
Insert into REQUIRED_SKILL (JOB_CODE,KS_CODE) values ('9','27');

-- PREFERRED_SKILL --
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('1','16');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('1','17');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('1','21');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('1','30');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('10','5');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('2','5');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('2','7');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('4','17');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('4','18');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('4','2');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('6','13');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('6','20');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('6','7');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('6','8');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('7','2');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('7','3');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('8','17');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('9','15');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('9','17');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('9','21');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('9','24');
Insert into PREFERRED_SKILL (JOB_CODE,KS_CODE) values ('9','9');

-- HAPPY PANDA --
